import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CaracteristicasEntrenamientoViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Este es un MutableStateFlow que alamcena una lista de objetos en este caso CaracteristicasEntrenamietos
     * -MutableStateFlow: Es un flujo en kotlin que mantiene un estado actual y emite actualizaciones cada vez que el
     * valor cambia.
     * En este caso se inizializa con emptyList, lo que significa que al inicio la lista está vacía.
     *
     * -_entrenamiento: Es una variable privada, lo que significa que solo se puede cambiar o modificar dentro del ViewModel
     *
     * <List<CaracteristicasEntrenamientos>>: Lista de objetos de la clase CaracteristicasEntrenamientos
     */
    private val _entrenamiento = MutableStateFlow<List<DataClassCaracteristicasEntrenamientos>>(emptyList())
    val entrenamiento: StateFlow<List<DataClassCaracteristicasEntrenamientos>> = _entrenamiento

    /**
     * Exponemos un StateFlow de solo lectura, para que otras partes del codigo como la UI puedan observarlo, pero sin poder
     * modificarlo directamente.
     */
    private val _rutinasGuardadas = MutableStateFlow<List<DataClassCaracteristicasEntrenamientos>>(emptyList())
    val rutinasGuardadas: StateFlow<List<DataClassCaracteristicasEntrenamientos>> = _rutinasGuardadas

    /**
     * Este es una variable que obtiene el ID del usuario autenticado actualmente en la aplicación.
     *
     * FirebaseAuth.getInstance(): Obtiene la instancia de Firebase Authentication. Esto te permite interactuar con el sistema de
     * autenticación de Firebase.
     *
     * currentUser: Esta propiedad da acceso al usuario actualmente autenticado, si el usuario no ha iniciado sesión este valor será de null
     *
     * ?.uid: ?. -> es un operador seguro de llamada, lo que significa que solo intentará acceder al uid si currentUSer no es null
     */
    private val userId: String? = FirebaseAuth.getInstance().currentUser?.uid

    /**
     * init: Es un bloque de codigo de inicialización en kotlin, que se ejecuta automáticamente cuando se crea una instancia del ViewModel.
     * Este sirve para poner cualquier codigo que quieras ejecutar justo después de que el viewModel haya sido creado.
     *
     * let{...} -> Es un bloque de codigo que se ejecuta si userId no es null. Es una forma segura de trabajar con valores opecionales. Dentro
     * del bloque let, el valor de userId se pasa como argumento en este caso el it.
     */
    init {
        userId?.let {
            getEntrenamiento(it)
            getRutinasGuardadas(it)
        }
    }

    /**
     * userSnapshot: Es el resultado de una consulta a Firestore para obtener un documento de la base de datos. En este caso,
     * userSnapshot es un objeto que representa el documento del usuario que estamos recuperando de Firestore.
     */
    fun getEntrenamiento(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entrenamientos = getEntrenamientosUsuario(userId)
                _entrenamiento.value = entrenamientos
                Log.d("FirestoreDebug", "Entrenamientos obtenidos: $entrenamientos")
            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error en getEntrenamiento: ${e.message}")
            }
        }
    }

    private suspend fun getEntrenamientosUsuario(userId: String): List<DataClassCaracteristicasEntrenamientos> {
        return try {
            val categoriasRef = db.collection("usuarios").document(userId)
            val userSnapshot = categoriasRef.get().await()

            if (!userSnapshot.exists()) {
                Log.d("FirestoreDebug", "No se encontró el documento del usuario.")
                return emptyList()
            }

            val categorias = userSnapshot.get("categorias") as? Map<String, List<String>> ?: emptyMap()
            Log.d("FirestoreDebug", "Categorías obtenidas: $categorias")

            categorias.map { (categoria, subcategorias) ->
                DataClassCaracteristicasEntrenamientos(categoria, subcategorias, "")
            }
        } catch (e: Exception) {
            Log.e("FirestoreDebug", "Error al obtener entrenamientos: ${e.message}")
            emptyList()
        }
    }

    fun guardarRutinaEnFirestore(
        userId: String,
        categoria: String,
        subcategoria: String,
        nombreEntrenamiento: String,
        diasSeleccionados: Set<String>,
        descanso: Int,
        repeticiones: Int,
        series: Int,
        bloqueTimestamp: Long,
        rutinaIndex: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocRef = db.collection("usuarios").document(userId)
                val rutina = mapOf(
                    "nombreEntrenamiento" to nombreEntrenamiento,
                    "categoria" to categoria,
                    "subcategoria" to subcategoria,
                    "dias" to diasSeleccionados.toList(),
                    "descanso" to descanso,
                    "repeticones" to repeticiones,
                    "series" to series
                )

                // Usando un identificador único para el bloque de entrenamiento
                val bloqueId = "bloqueEntrenamiento_$bloqueTimestamp"
                val rutinaKey = "rutina_$rutinaIndex"

                // Obtener el documento del usuario
                val snapshot = userDocRef.get().await()
                val rutinasGuardadas: MutableMap<String, Any> = mutableMapOf()

                // Si el documento existe, obtenemos las rutinas guardadas
                if (snapshot.exists()) {
                    val rutinas = snapshot.get("rutinasGuardadas") as? Map<String, Any> ?: mutableMapOf()

                    // Verificamos si el bloque ya existe
                    if (rutinas.containsKey(bloqueId)) {
                        val bloqueContent = rutinas[bloqueId] as? MutableMap<String, Any> ?: mutableMapOf()
                        // Añadimos o actualizamos la rutina dentro del bloque
                        bloqueContent[rutinaKey] = listOf(rutina)
                        rutinasGuardadas[bloqueId] = bloqueContent
                    } else {
                        // Si no existe el bloque, lo creamos con la nueva rutina
                        rutinasGuardadas[bloqueId] = mapOf(rutinaKey to listOf(rutina))
                    }
                } else {
                    // Si el documento no existe, creamos una nueva estructura
                    rutinasGuardadas[bloqueId] = mapOf(rutinaKey to listOf(rutina))
                }

                // Guardamos o actualizamos el documento con las rutinas
                userDocRef.set(
                    mapOf(
                        "rutinasGuardadas" to rutinasGuardadas,
                        "fechaCreacion" to FieldValue.serverTimestamp()
                    ),
                    SetOptions.merge()
                ).await()

                Log.d("FirestoreDebug", "Rutina guardada correctamente con nueva estructura.")
            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error al guardar nueva rutina: ${e.message}")
            }
        }
    }


    private fun getRutinasGuardadas(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Obtener documento de usuario desde Firestore
                val userDoc = db.collection("usuarios").document(userId).get().await()

                // Obtener rutinas guardadas en el documento del usuario
                val rutinasMap = userDoc.get("rutinasGuardadas") as? Map<String, Map<String, List<Map<String, Any>>>> ?: emptyMap()

                // Convertir las rutinas a una lista plana
                val rutinas = rutinasMap.flatMap { (bloqueId, rutinaSet) ->
                    rutinaSet.flatMap { (rutinaKey, rutinaList) ->
                        rutinaList.mapNotNull { rutina ->
                            val nombre = rutina["nombreEntrenamiento"] as? String
                            val categoria = rutina["categoria"] as? String
                            val subcategoria = rutina["subcategoria"] as? String
                            val dias = rutina["dias"] as? List<String>

                            if (nombre != null && subcategoria != null) {
                                DataClassCaracteristicasEntrenamientos(
                                    nombre = nombre,
                                    categoria = categoria,
                                    subcategorias = listOf(subcategoria),
                                    nombreEntrenamiento = nombre,
                                    dias = dias,
                                    bloqueId = bloqueId,
                                    rutinaKey = rutinaKey
                                )
                            } else null
                        }
                    }
                }

                _rutinasGuardadas.value = rutinas
                Log.d("FirestoreDebug", "Rutinas cargadas: $rutinas")

            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error al obtener rutinas guardadas: ${e.message}")
            }
        }
    }




    fun actualizarEjercicio(
        bloqueId: String,
        rutinaKey: String,
        ejercicioActual: DataClassCaracteristicasEntrenamientos,
        nuevoNombre: String,
        nuevaCategoria: String,
        nuevaSubcategoria: String,
        nuevosDias: List<String>,
        nuevoDescanso: Int,
        nuevasRepeticiones: Int,
        nuevasSeries: Int
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocRef = db.collection("usuarios").document(userId)

                // Obtener el documento actual
                val snapshot = userDocRef.get().await()
                if (!snapshot.exists()) {
                    Log.d("Firestore", "Documento de usuario no existe")
                    return@launch
                }

                // Obtener todas las rutinas guardadas
                val rutinasGuardadas = snapshot.get("rutinasGuardadas") as? MutableMap<String, Any>
                    ?: mutableMapOf()

                // Obtener el bloque específico
                val bloque = rutinasGuardadas[bloqueId] as? MutableMap<String, Any>
                    ?: mutableMapOf()

                // Obtener la rutina específica
                val rutinaList = bloque[rutinaKey] as? List<Map<String, Any>>
                    ?: emptyList()

                if (rutinaList.isNotEmpty()) {
                    val rutinaActual = rutinaList[0].toMutableMap().apply {
                        this["nombreEntrenamiento"] = nuevoNombre
                        this["categoria"] = nuevaCategoria
                        this["subcategoria"] = nuevaSubcategoria
                        this["dias"] = nuevosDias
                        this["descanso"] = nuevoDescanso
                        this["repeticiones"] = nuevasRepeticiones
                        this["series"] = nuevasSeries
                    }

                    bloque[rutinaKey] = listOf(rutinaActual)
                    rutinasGuardadas[bloqueId] = bloque

                    userDocRef.update("rutinasGuardadas", rutinasGuardadas)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Ejercicio actualizado correctamente")
                            getRutinasGuardadas(userId)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error al actualizar: ${e.message}", e)
                        }
                } else {
                    Log.d("Firestore", "No se encontró la rutina para actualizar")
                }
            } catch (e: Exception) {
                Log.e("Firestore", "Error en actualizarEjercicio", e)
            }
        }
    }
//    fun getRutinasPorBloque(userId: String, bloqueId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val userDocRef = db.collection("usuarios").document(userId)
//                val snapshot = userDocRef.get().await()
//
//                val rutinas = snapshot.get("rutinasGuardadas") as? Map<String, Map<String, List<Map<String, Any>>>> ?: emptyMap()
//
//                val rutinasPorBloque = rutinas[bloqueId]?.mapNotNull { (rutinaKey, rutinaList) ->
//                    val rutina = rutinaList.firstOrNull()
//                    rutina?.let {
//                        val nombre = it["nombreEntrenamiento"] as? String
//                        val categoria = it["categoria"] as? String
//                        val subcategoria = it["subcategoria"] as? String
//                        val dias = it["dias"] as? List<String>
//
//                        DataClassCaracteristicasEntrenamientos(
//                            nombre = nombre,
//                            categoria = categoria,
//                            subcategorias = listOf(subcategoria ?: ""),
//                            nombreEntrenamiento = nombre,
//                            dias = dias
//                        )
//                    }
//                } ?: emptyList()
//
//                _entrenamiento.value = rutinasPorBloque
//                Log.d("FirestoreDebug", "Rutinas para bloque $bloqueId: $rutinasPorBloque")
//
//            } catch (e: Exception) {
//                Log.e("FirestoreDebug", "Error al obtener rutinas por bloque: ${e.message}")
//            }
//        }
//    }
//
//    fun obtenerRutinaPorKey(rutinaKey: String): DataClassCaracteristicasEntrenamientos? {
//        Log.d("ObtenerRutinaPorKey", rutinaKey)
//        return _rutinasGuardadas.value.find { it.rutinaKey == rutinaKey }
//    }
}