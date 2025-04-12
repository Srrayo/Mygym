package com.example.mygym.model

data class Rutina(
    val nombreRutina: String,
    val entrenamientos: List<Entrenamiento> = emptyList()
)
