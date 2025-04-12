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
        bloqueTimestamp: Long,
        rutinaIndex: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocRef = db.collection("usuarios").document(userId)

                // Creando el objeto de rutina
                val rutina = mapOf(
                    "nombreEntrenamiento" to nombreEntrenamiento,
                    "categoria" to categoria,
                    "subcategoria" to subcategoria,
                    "dias" to diasSeleccionados.toList()  // Convirtiendo el set a lista
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
                    SetOptions.merge()  // Mantenemos los datos existentes y solo actualizamos lo necesario
                ).await()

                Log.d("FirestoreDebug", "Rutina guardada correctamente con nueva estructura.")
            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error al guardar nueva rutina: ${e.message}")
            }
        }
    }



//    fun getRutinasGuardadas(userId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val userDoc = db.collection("usuarios").document(userId).get().await()
//                val rutinasGuardadasMap = userDoc.get("rutinasGuardadas") as? Map<*, *> ?: emptyMap<Any, Any>()
//
//                val rutinas = mutableListOf<DataClassCaracteristicasEntrenamientos>()
//
//                for ((_, bloque) in rutinasGuardadasMap) {
//                    val bloqueMap = bloque as? Map<*, *> ?: continue
//
//                    for ((_, rutinaList) in bloqueMap) {
//                        val rutinasDentro = rutinaList as? List<Map<String, Any>> ?: continue
//
//                        for (rutina in rutinasDentro) {
//                            val categoria = rutina["categoria"] as? String
//                            val subcategoria = rutina["subcategoria"] as? String
//                            val nombre = rutina["nombreEntrenamiento"] as? String
//                            val dias = rutina["dias"] as? List<String>
//
//                            if (categoria != null && subcategoria != null && nombre != null) {
//                                rutinas.add(
//                                    DataClassCaracteristicasEntrenamientos(
//                                        nombre = categoria,
//                                        subcategorias = listOf(subcategoria),
//                                        nombreEntrenamiento = nombre,
//                                        dias = dias,
//                                        categoria = categoria
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//
//                _rutinasGuardadas.value = rutinas
//                Log.d("FirestoreDebug", "Rutinas guardadas obtenidas: $rutinas")
//            } catch (e: Exception) {
//                Log.e("FirestoreDebug", "Error al obtener rutinas guardadas: ${e.message}")
//            }
//        }
//    }



    fun getRutinasGuardadas(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDoc = db.collection("usuarios").document(userId).get().await()
                val rutinasMap = userDoc.get("rutinasGuardadas") as? Map<String, Map<String, List<Map<String, Any>>>> ?: emptyMap()

                val rutinas = rutinasMap.mapNotNull { (bloqueId, rutinaSet) ->
                    val firstRutina = rutinaSet.values.firstOrNull()?.firstOrNull()
                    firstRutina?.let { rutina ->
                        val nombre = rutina["nombreEntrenamiento"] as? String
                        val subcategoria = rutina["subcategoria"] as? String
                        val dias = rutina["dias"] as? List<String>
                        if (nombre != null && subcategoria != null) {
                            DataClassCaracteristicasEntrenamientos(
                                nombre = null,
                                subcategorias = listOf(subcategoria),
                                nombreEntrenamiento = nombre,
                                dias = dias,
                                categoria = bloqueId // Mostramos el bloque como referencia
                            )
                        } else null
                    }
                }

                _rutinasGuardadas.value = rutinas
                Log.d("FirestoreDebug", "Rutinas cargadas (una por bloque): $rutinas")

            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error al obtener rutinas guardadas: ${e.message}")
            }
        }
    }

}