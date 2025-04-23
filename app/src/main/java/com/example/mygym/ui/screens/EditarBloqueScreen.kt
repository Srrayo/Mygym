package com.example.mygym.ui.EditarEntrenamiento

import CaracteristicasEntrenamientoViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.ui.screens.RutinaCardEntrenamiento
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos

@Composable
fun EditarBloqueScreen(
    bloqueId: String,
    viewModel: CaracteristicasEntrenamientoViewModel,
    navController: NavController
) {
    // Obtener las rutinas guardadas
    val rutinas by viewModel.rutinasGuardadas.collectAsState()

    // Filtrar las rutinas por bloqueId
    val rutinasDelBloque = rutinas.filter { it.bloqueId == bloqueId }

    Column(modifier = Modifier.padding(16.dp)) {
        // Título de la pantalla
        Text("Editar bloque: $bloqueId", fontSize = 20.sp)

        // Lista de rutinas del bloque
        LazyColumn {
            items(rutinasDelBloque) { rutina ->
                val subcategoria = rutina.subcategorias?.firstOrNull() ?: ""
                val diasString = rutina.dias?.joinToString(",") ?: ""

                // Llamada al card de rutina, pasando el bloqueId y el onClick
                RutinaCardEntrenamiento(bloqueId = rutina.bloqueId ?: "Desconocido") {
                    // Redirigir a la pantalla de edición del ejercicio, pasando parámetros
                    navController.navigate(
                        "editar_ejercicio/${rutina.bloqueId}/${rutina.nombreEntrenamiento}/${rutina.categoria}/${subcategoria}/${diasString}/${rutina.descanso}/${rutina.repeticiones}/${rutina.series}"
                    )
                }
            }
        }
    }
}
