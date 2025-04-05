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

    fun guardarRutinaEnFirestore(userId: String, categoria: String, subcategoria: String, nombreEntrenamiento: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocRef = db.collection("usuarios").document(userId)
                val rutina = mapOf(
                    "nombreEntrenamiento" to nombreEntrenamiento,
                    "categoria" to categoria,
                    "subcategoria" to subcategoria
                )

                Log.d("FirestoreDebug", "Guardando rutina: $rutina")

                val documentSnapshot = userDocRef.get().await()

                if (documentSnapshot.exists() && documentSnapshot.contains("rutinasGuardadas")) {
                    userDocRef.update("rutinasGuardadas", FieldValue.arrayUnion(rutina)).await()
                    userDocRef.update("fechaCreacion", FieldValue.serverTimestamp()).await()
                    Log.d("FirestoreDebug", "Rutina y fecha actualizadas correctamente.")
                } else {
                    userDocRef.set(
                        mapOf(
                            "rutinasGuardadas" to listOf(rutina),
                            "fecha_creacion" to FieldValue.serverTimestamp()
                        ),
                        SetOptions.merge()
                    ).await()
                    Log.d("FirestoreDebug", "Rutina y fecha guardadas correctamente.")
                }
            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error en guardarRutinaEnFirestore: ${e.message}")
            }
        }
    }

    fun getRutinasGuardadas(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDoc = db.collection("usuarios").document(userId).get().await()
                val rutinasGuardadas = userDoc.get("rutinasGuardadas") as? List<Map<String, Any>> ?: emptyList()

                val rutinas = rutinasGuardadas.mapNotNull { rutina ->
                    val categoria = rutina["categoria"] as? String
                    val subcategoria = rutina["subcategoria"] as? String
                    val nombre = rutina["nombreEntrenamiento"] as? String
                    if (categoria != null && subcategoria != null && nombre != null) {
                        DataClassCaracteristicasEntrenamientos(categoria, listOf(subcategoria), nombre)
                    } else {
                        null
                    }
                }

                _rutinasGuardadas.value = rutinas
                Log.d("FirestoreDebug", "Rutinas guardadas obtenidas: $rutinas")
            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error al obtener rutinas guardadas: ${e.message}")
            }
        }
    }
}
