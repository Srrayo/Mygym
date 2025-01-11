package com.example.mygym.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var entrenamientos by mutableStateOf(emptyList<Entrenamientos>())
    var nombre by mutableStateOf("")
    var dia by mutableStateOf("")
    var entrenamiento by mutableStateOf("")
    fun agregarEntrenamiento(entrenamiento: Entrenamientos) {
        entrenamientos = entrenamientos + entrenamiento
    }

    var nombreUsuario = "USER"
    var contrasenaUsuario = "CONTRASENA"

    var entrenamientoUser by mutableStateOf(
        listOf(
            Entrenamientos(
                id = 0,
                nombre,
                dia,
                entrenamiento,
                categoriaEntrenamientos = listOf(
                    CategoriaEntrenamientos.GIMNASIO,
                    CategoriaEntrenamientos.CARDIO,
                    CategoriaEntrenamientos.FUERZA,
                    CategoriaEntrenamientos.FLEXIBILIDAD,
                    CategoriaEntrenamientos.ENTRENAMIENTOSMIXTOS
                )
            )
        )
    )

}