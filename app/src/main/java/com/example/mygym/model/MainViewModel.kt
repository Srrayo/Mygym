package com.example.mygym.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var entrenamientos by mutableStateOf(emptyList<DataClassCaracteristicasEntrenamientos>())
    var nombre by mutableStateOf("")
    var dia by mutableStateOf("")
    var entrenamiento by mutableStateOf("")
    var nombreUsuario = "USER"



}