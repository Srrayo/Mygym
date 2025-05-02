package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun EditarRutinaScreen(rutinaKey: String, viewModel: CaracteristicasEntrenamientoViewModel) {
    val rutina = viewModel.rutinasGuardadas.collectAsState().value.firstOrNull { it.rutinaKey == rutinaKey }

    if (rutina != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Editar Rutina: ${rutina.nombreEntrenamiento}")

            var nombre by remember { mutableStateOf(rutina.nombreEntrenamiento) }
            nombre?.let {
                TextField(
                    value = it,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la rutina") }
                )
            }

            var categoria by remember { mutableStateOf(rutina.categoria ?: "") }
            TextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") }
            )

            var subcategoria by remember { mutableStateOf(rutina.subcategorias?.firstOrNull() ?: "") }
            TextField(
                value = subcategoria,
                onValueChange = { subcategoria = it },
                label = { Text("Subcategoría") }
            )

            var diasSeleccionados by remember { mutableStateOf(rutina.dias ?: emptyList()) }

            Button(
                onClick = {
                    rutina.bloqueId?.let {
                        rutina.rutinaKey?.let { it1 ->
                            nombre?.let { it2 ->
                                viewModel.actualizarEjercicio(
                                    it,
                                    it1,
                                    rutina,
                                    it2,
                                    categoria,
                                    subcategoria,
                                    diasSeleccionados,
                                    60,
                                    10,
                                    4
                                )
                            }
                        }
                    }
                }
            ) {
                Text(text = "Guardar cambios")
            }
        }
    } else {
        Text(text = "Rutina no encontrada", color = Color.Red)
    }
}



