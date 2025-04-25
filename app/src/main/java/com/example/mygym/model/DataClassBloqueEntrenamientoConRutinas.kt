package com.example.mygym.model

data class BloqueEntrenamientoConRutinas(
    val bloqueId: String,
    val rutinas: List<DataClassCaracteristicasEntrenamientos>
)