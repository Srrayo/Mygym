import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygym.model.CaracteristicasEntrenamientos
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

    private val _entrenamiento = MutableStateFlow<List<CaracteristicasEntrenamientos>>(emptyList())
    val entrenamiento: StateFlow<List<CaracteristicasEntrenamientos>> = _entrenamiento

    private val _rutinasGuardadas = MutableStateFlow<List<CaracteristicasEntrenamientos>>(emptyList())
    val rutinasGuardadas: StateFlow<List<CaracteristicasEntrenamientos>> = _rutinasGuardadas

    private val userId: String? = FirebaseAuth.getInstance().currentUser?.uid

    init {
        userId?.let {
            getEntrenamiento(it)
            getRutinasGuardadas(it)
        }
    }

    fun getEntrenamiento(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entrenamientos = getEntrenamientosUsuario(userId)
                _entrenamiento.value = entrenamientos
            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error en getEntrenamiento: ${e.message}")
            }
        }
    }

    private suspend fun getEntrenamientosUsuario(userId: String): List<CaracteristicasEntrenamientos> {
        return try {
            val categoriasRef = db.collection("usuarios").document(userId)
            val userSnapshot = categoriasRef.get().await()

            if (!userSnapshot.exists()) return emptyList()

            val categorias = userSnapshot.get("categorias") as? Map<String, List<String>> ?: emptyMap()
            categorias.map { (categoria, subcategorias) ->
                CaracteristicasEntrenamientos(categoria, subcategorias)
            }
        } catch (e: Exception) {
            Log.e("FirestoreDebug", "Error al obtener entrenamientos: ${e.message}")
            emptyList()
        }
    }

    fun guardarRutinaEnFirestore(userId: String, categoria: String, subcategoria: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocRef = db.collection("usuarios").document(userId)
                val rutina = mapOf(
                    "categoria" to categoria,
                    "subcategoria" to subcategoria
                )
                val fechaCreacion = mapOf(
                    "fecha_creacion" to FieldValue.serverTimestamp()
                )

                // Primero obtenemos el documento para verificar si "rutinasGuardadas" existe
                userDocRef.get().addOnSuccessListener { document ->
                    if (document.exists() && document.contains("rutinasGuardadas")) {
                        // Si existe, actualizamos el campo rutinasGuardadas
                        userDocRef.update("rutinasGuardadas", FieldValue.arrayUnion(rutina))
                            .addOnSuccessListener {
                                // También agregamos la fecha de creación en otro campo
                                userDocRef.update("fechaCreacion", FieldValue.serverTimestamp())
                                    .addOnSuccessListener {
                                        Log.d("FirestoreDebug", "Fecha de creación guardada correctamente.")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("FirestoreDebug", "Error al guardar fecha de creación: ${e.message}")
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirestoreDebug", "Error al guardar rutina: ${e.message}")
                            }
                    } else {
                        // Si no existe, creamos el campo rutinasGuardadas y fecha_creacion
                        userDocRef.set(
                            mapOf(
                                "rutinasGuardadas" to listOf(rutina),
                                "fecha_creacion" to FieldValue.serverTimestamp()
                            ), SetOptions.merge()
                        ).addOnSuccessListener {
                            Log.d("FirestoreDebug", "Rutina y fecha guardada correctamente.")
                        }.addOnFailureListener { e ->
                            Log.e("FirestoreDebug", "Error al crear rutinasGuardadas y fecha: ${e.message}")
                        }
                    }
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
                    if (categoria != null && subcategoria != null) {
                        CaracteristicasEntrenamientos(categoria, listOf(subcategoria))
                    } else {
                        null
                    }
                }

                _rutinasGuardadas.value = rutinas
            } catch (e: Exception) {
                Log.e("FirestoreDebug", "Error al obtener rutinas guardadas: ${e.message}")
            }
        }
    }
}