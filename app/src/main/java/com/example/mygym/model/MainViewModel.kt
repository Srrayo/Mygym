package com.example.mygym.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var entrenamientos by mutableStateOf(
        listOf<Entrenamientos>(
            Entrenamientos(
                dia = "Lunes",
                entrenamiento = "Espalda",
                empezar = "Iniciar"
            ),
            Entrenamientos(
                dia = "Martes",
                entrenamiento = "Pecho",
                empezar = "Iniciar"
            ),
            Entrenamientos(
                dia = "Miercoles",
                entrenamiento = "Pierna",
                empezar = "Iniciar"
            ),
            Entrenamientos(
                dia = "Jueves",
                entrenamiento = "Espalda",
                empezar = "Iniciar"
            ),
            Entrenamientos(
                dia = "Viernes",
                entrenamiento = "Pecho",
                empezar = "Iniciar"
            ),
        )
    )
}