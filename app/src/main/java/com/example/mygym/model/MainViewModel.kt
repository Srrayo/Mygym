package com.example.mygym.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var entrenamientos by mutableStateOf(emptyList<Entrenamientos>())

    var nombre by mutableStateOf("")
    var dia by mutableStateOf("")
    var entrenamiento by mutableStateOf("")

    var nombreUsuario = "USER"
    var contrasenaUsuario = "CONTRASENA"

}