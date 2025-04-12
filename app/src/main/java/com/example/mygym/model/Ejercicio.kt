package com.example.mygym.model

data class Ejercicio(
    val nombreEjercicio: String,
    val series: Int,
    val repeticiones: Int,
    val peso: Float? // Opcional
)
