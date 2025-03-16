package com.example.mygym.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var entrenamientos by mutableStateOf(emptyList<CaracteristicasEntrenamientos>())
    var nombre by mutableStateOf("")
    var dia by mutableStateOf("")
    var entrenamiento by mutableStateOf("")
    var nombreUsuario = "USER"



}