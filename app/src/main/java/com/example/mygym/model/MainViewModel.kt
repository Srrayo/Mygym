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

    var dias by mutableStateOf(
        Dias(
            Lunes = "Lunes",
            Martes = "Martes",
            Miercoles = "Miércoles",
            Jueves = "Jueves",
            Viernes = "Viernes",
            Sabado = "Sábado",
            Domingo = "Domingo"
        )
    )


    var nombreUsuario = "USER"
    var contrasenaUsuario = "CONTRASENA"

}