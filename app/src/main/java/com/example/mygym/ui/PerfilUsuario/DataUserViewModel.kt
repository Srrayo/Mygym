package com.example.mygym.ui.PerfilUsuario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygym.model.DataClassUsuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class DataUserViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    var usuario by mutableStateOf(DataClassUsuario())

    fun loadUserData(userId: String) {
        viewModelScope.launch {
            try {
                val userDoc = db.collection("usuarios").document(userId)
                userDoc.get().addOnSuccessListener { document ->
                    if (document != null) {
                        usuario = document.toObject(DataClassUsuario::class.java) ?: DataClassUsuario()
                    } else {
                        usuario = DataClassUsuario()
                    }
                }.addOnFailureListener {
                    usuario = DataClassUsuario()
                }
            } catch (e: Exception) {
                usuario = DataClassUsuario()
            }
        }
    }

    fun updateUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                val userDoc = db.collection("usuarios").document(userId)
                val updatedData = mapOf(
                    "nombre" to usuario.nombre,
                    "apellidos" to usuario.apellidos,
                    "edad" to usuario.edad,
                    "peso" to usuario.peso,
                    "altura" to usuario.altura,
                    "fechaNacimiento" to usuario.fechaNacimiento
                )
                userDoc.update(updatedData)
            } catch (e: Exception) {
            }
        }
    }
}
