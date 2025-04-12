package com.example.mygym.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class CalendarViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _fechasConEntrenamiento = MutableStateFlow<List<LocalDate>>(emptyList())
    val fechasConEntrenamiento = _fechasConEntrenamiento.asStateFlow()

    // Función para guardar el entrenamiento en Firestore
    @RequiresApi(Build.VERSION_CODES.O)
    fun guardarEntrenamiento(fecha: LocalDate, nota: String) {
        val entrenamiento = DataClassFechaEntrenamiento(
            fecha = fecha.toString(),  // yyyy-MM-dd
            nota = nota
        )

        db.collection("entrenamientos")
            .add(entrenamiento)
            .addOnSuccessListener {
                cargarEntrenamientos()
            }
            .addOnFailureListener {
                Log.e("CalendarVM", "Error al guardar", it)
            }
    }

    // Función para cargar los entrenamientos desde Firestore
    @RequiresApi(Build.VERSION_CODES.O)
    fun cargarEntrenamientos() {
        db.collection("entrenamientos")
            .get()
            .addOnSuccessListener { result ->
                val fechas = result.mapNotNull {
                    it.getString("fecha")?.let { fechaStr ->
                        try {
                            LocalDate.parse(fechaStr)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
                _fechasConEntrenamiento.value = fechas
            }
    }
}
