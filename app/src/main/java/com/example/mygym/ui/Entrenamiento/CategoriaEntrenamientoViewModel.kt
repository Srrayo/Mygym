import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygym.model.CaracteristicasEntrenamientos
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CaracteristicasEntrenamientoViewModel : ViewModel() {
    private var db: FirebaseFirestore = Firebase.firestore
    private val _entrenamiento = MutableStateFlow<List<CaracteristicasEntrenamientos>>(emptyList())
    val entrenamiento: StateFlow<List<CaracteristicasEntrenamientos>> = _entrenamiento

    // Nuevo StateFlow para rutinas guardadas
    private val _rutinasGuardadas = MutableStateFlow<List<CaracteristicasEntrenamientos>>(emptyList())
    val rutinasGuardadas: StateFlow<List<CaracteristicasEntrenamientos>> = _rutinasGuardadas

    init {
        getEntrenamiento()
    }

    private fun getEntrenamiento() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getAllEntrenamientos()
            }
            _entrenamiento.value = result
        }
    }

    private suspend fun getAllEntrenamientos(): List<CaracteristicasEntrenamientos> {
        return try {
            db.collection("Categorias")
                .get()
                .await()
                .documents
                .mapNotNull { doc -> doc.toObject(CaracteristicasEntrenamientos::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Método para guardar una rutina en el usuario
    fun guardarRutinaEnUsuario(userId: String, rutinaId: String) {
        viewModelScope.launch {
            try {
                val userDoc = db.collection("Usuarios").document(userId)
                userDoc.update("rutinasGuardadas", FieldValue.arrayUnion(rutinaId))
                    .addOnSuccessListener { Log.d("Firebase", "Rutina guardada correctamente") }
                    .addOnFailureListener { e -> Log.e("Firebase", "Error al guardar rutina", e) }
            } catch (e: Exception) {
                Log.e("Firebase", "Error: ${e.message}")
            }
        }
    }

    // Método para obtener las rutinas guardadas
    fun getRutinasGuardadas(userId: String) {
        viewModelScope.launch {
            try {
                // Obtener las rutinas guardadas del documento de usuario
                val userDoc = db.collection("Usuarios").document(userId).get().await()
                val rutinasIds = userDoc.get("rutinasGuardadas") as? List<String> ?: emptyList()

                // Obtener cada rutina usando sus IDs
                val rutinas = rutinasIds.mapNotNull { rutinaId ->
                    db.collection("Rutinas").document(rutinaId).get().await()
                        .toObject(CaracteristicasEntrenamientos::class.java)
                }
                // Actualizar el _rutinasGuardadas StateFlow
                _rutinasGuardadas.value = rutinas
            } catch (e: Exception) {
                Log.e("Firebase", "Error al obtener rutinas guardadas: ${e.message}")
            }
        }
    }
}
