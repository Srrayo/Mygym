package com.example.mygym.model

data class DataClassCaracteristicasEntrenamientos(
    val nombre: String?,
    val subcategorias: List<String>?,
    val nombreEntrenamiento: String?,
    val dias: List<String>? = null,
    val categoria: String? = null,
    val descanso: Int = 0,
    val repeticiones: Int = 0,
    val series: Int = 0,
    val bloqueId: String? = null

)