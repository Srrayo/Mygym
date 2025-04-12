package com.example.mygym.model

data class Entrenamiento(
    val nombre: String?,
    val subcategorias: List<String>?,
    val nombreEntrenamiento: String?,
    val ejercicios: List<Ejercicio> = emptyList(),
    val dias: List<String>? = null
)

