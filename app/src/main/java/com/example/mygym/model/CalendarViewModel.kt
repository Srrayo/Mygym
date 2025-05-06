
package com.example.mygym.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _rutinasGuardadas = MutableStateFlow<List<DataClassCaracteristicasEntrenamientos>>(emptyList())
    val rutinasGuardadas: StateFlow<List<DataClassCaracteristicasEntrenamientos>> = _rutinasGuardadas

    private val _fechasConEntrenamiento = MutableStateFlow<Set<LocalDate>>(emptySet())
    val fechasConEntrenamiento: StateFlow<Set<LocalDate>> = _fechasConEntrenamiento.asStateFlow()

    private val _notasEntrenamiento = MutableStateFlow<Map<LocalDate, String>>(emptyMap())
    val notasEntrenamiento: StateFlow<Map<LocalDate, String>> = _notasEntrenamiento.asStateFlow()


    private val userId: String? = FirebaseAuth.getInstance().currentUser?.uid

    private val dayOfWeekMap = mapOf(
        "L" to DayOfWeek.MONDAY,
        "M" to DayOfWeek.TUESDAY,
        "X" to DayOfWeek.WEDNESDAY,
        "J" to DayOfWeek.THURSDAY,
        "V" to DayOfWeek.FRIDAY,
        "S" to DayOfWeek.SATURDAY,
        "D" to DayOfWeek.SUNDAY
    )

    init {
        cargarEntrenamientos()
        cargarNotas()
        userId?.let {
            getRutinasGuardadasSoloHoy(it)
        }
    }

    private fun getUsuarioDocRef() = auth.currentUser?.uid?.let { db.collection("usuarios").document(it) }

    fun cargarEntrenamientos() {
        val userDocRef = getUsuarioDocRef() ?: return

        viewModelScope.launch {
            try {
                val snapshot = userDocRef.get().await()
                val rutinasGuardadas = snapshot.get("rutinasGuardadas") as? Map<String, Map<String, List<Map<String, Any>>>> ?: emptyMap()
                val fechas = mutableSetOf<LocalDate>()
                val currentMonth = YearMonth.now()

                rutinasGuardadas.forEach { (_, bloque) ->
                    bloque.forEach { (_, rutinas) ->
                        rutinas.forEach { rutina ->
                            val diasRutina = rutina["dias"] as? List<String> ?: emptyList()
                            diasRutina.forEach { diaAbbr ->
                                dayOfWeekMap[diaAbbr]?.let { dayOfWeek ->
                                    var date = currentMonth.atDay(1)
                                    while (date.month == currentMonth.month) {
                                        if (date.dayOfWeek == dayOfWeek) {
                                            fechas.add(date)
                                        }
                                        date = date.plusDays(1)
                                    }
                                }
                            }
                        }
                    }
                }

                _fechasConEntrenamiento.value = fechas
            } catch (e: Exception) {
                Log.e("CalendarVM", "Error al cargar entrenamientos", e)
            }
        }
    }

    fun guardarEntrenamiento(fecha: LocalDate, nota: String) {
        val userDocRef = getUsuarioDocRef() ?: return

        viewModelScope.launch {
            try {
                val notaData = hashMapOf(
                    "fecha" to fecha.toString(),
                    "nota" to nota,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                userDocRef.set(
                    hashMapOf(
                        "notasCalendario" to hashMapOf(
                            fecha.toString() to notaData
                        )
                    ),
                    SetOptions.merge()
                ).await()

                _notasEntrenamiento.value += (fecha to nota)

            } catch (e: Exception) {
                Log.e("CalendarVM", "Error al guardar nota", e)
            }
        }
    }


    fun cargarNotas() {
        val userDocRef = getUsuarioDocRef() ?: return

        viewModelScope.launch {
            try {
                val snapshot = userDocRef.get().await()
                val notasMap = snapshot.get("notasCalendario") as? Map<String, Map<String, Any>> ?: emptyMap()

                val notas = notasMap.mapNotNull { (fechaStr, notaData) ->
                    try {
                        LocalDate.parse(fechaStr) to (notaData["nota"] as? String ?: "")
                    } catch (e: Exception) {
                        null
                    }
                }.toMap()

                _notasEntrenamiento.value = notas
            } catch (e: Exception) {
                Log.e("CalendarVM", "Error al cargar notas", e)
            }
        }
    }

    fun eliminarNota(fecha: LocalDate) {
        val userDocRef = getUsuarioDocRef() ?: return

        viewModelScope.launch {
            try {
                // Eliminar la nota específica usando FieldValue.delete()
                val updates = hashMapOf<String, Any>(
                    "notasCalendario.${fecha.toString()}" to FieldValue.delete()
                )

                userDocRef.update(updates).await()

                // Actualizar estado local
                _fechasConEntrenamiento.value -= fecha
                _notasEntrenamiento.value -= fecha
            } catch (e: Exception) {
                Log.e("CalendarVM", "Error al eliminar nota", e)
            }
        }
    }

    fun tieneEntrenamiento(fecha: LocalDate): Boolean {
        return _fechasConEntrenamiento.value.contains(fecha)
    }


    private fun getRutinasGuardadasSoloHoy(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDoc = db.collection("usuarios").document(userId).get().await()
                val rutinasMap = userDoc.get("rutinasGuardadas") as? Map<String, Map<String, List<Map<String, Any>>>> ?: emptyMap()

                val today = LocalDate.now().dayOfWeek
                val diaAbreviado = dayOfWeekMap.entries.find { it.value == today }?.key

                if (diaAbreviado == null) return@launch

                val rutinas = rutinasMap.flatMap { (bloqueId, rutinaSet) ->
                    rutinaSet.flatMap { (rutinaKey, rutinaList) ->
                        rutinaList.mapNotNull { rutina ->
                            val dias = rutina["dias"] as? List<String> ?: return@mapNotNull null
                            if (diaAbreviado in dias) {
                                val nombre = rutina["nombreEntrenamiento"] as? String
                                val categoria = rutina["categoria"] as? String
                                val subcategoria = rutina["subcategoria"] as? String
                                val descanso = (rutina["descanso"] as? Long)?.toInt() ?: 0
                                val series = (rutina["series"] as? Long)?.toInt() ?: 0
                                val repeticiones = (rutina["repeticiones"] as? Long)?.toInt() ?: 0

                                if (nombre != null && subcategoria != null) {
                                    DataClassCaracteristicasEntrenamientos(
                                        nombre = nombre,
                                        categoria = categoria,
                                        subcategorias = listOf(subcategoria),
                                        nombreEntrenamiento = nombre,
                                        dias = dias,
                                        descanso = descanso,
                                        repeticiones = repeticiones,
                                        series = series,
                                        bloqueId = bloqueId,
                                        rutinaKey = rutinaKey
                                    )
                                } else null
                            } else null
                        }
                    }
                }

                _rutinasGuardadas.value = rutinas
                Log.d("FirestoreDebug", "Rutinas de hoy: $rutinas")

            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error al obtener rutinas del día: ${e.message}")
            }
        }
    }
}