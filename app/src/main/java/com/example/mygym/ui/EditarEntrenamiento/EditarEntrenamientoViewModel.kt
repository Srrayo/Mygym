package com.example.mygym.ui.EditarEntrenamiento

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.google.firebase.firestore.FirebaseFirestore

class EditarEntrenamientoViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
//    var usuario by mutableStateOf(DataClassCaracteristicasEntrenamientos())
}