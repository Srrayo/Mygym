package com.example.mygym.model

data class Entrenamientos (
    val id: Int,
    val nombre: String,
    val dia: String,
    val entrenamiento: String,
    val categoriaEntrenamientos: List<CategoriaEntrenamientos>
)