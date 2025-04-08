package com.example.mygym.model

data class DataClassCaracteristicasEntrenamientos(
    val nombre: String?,
    val subcategorias: List<String>?,
    val nombreEntrenamiento: String?,
    val dias: List<String>? = null
)