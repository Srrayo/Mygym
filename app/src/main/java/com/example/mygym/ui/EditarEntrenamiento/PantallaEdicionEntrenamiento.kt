
package com.example.mygym.ui.EditarEntrenamiento

import CaracteristicasEntrenamientoViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos

@Composable
fun PantallaEdicionEjercicio(
    ejercicio: DataClassCaracteristicasEntrenamientos,
    viewModel: CaracteristicasEntrenamientoViewModel,
    navController: NavController,
    rutinaKey: String
) {
    var nombre by remember { mutableStateOf(ejercicio.nombreEntrenamiento ?: "") }
    var categoria by remember { mutableStateOf(ejercicio.categoria ?: "") }
    var subcategoria by remember { mutableStateOf(ejercicio.subcategorias?.firstOrNull() ?: "") }
    var dias by remember { mutableStateOf(ejercicio.dias ?: emptyList()) }
    var descanso by remember { mutableStateOf(ejercicio.descanso) }
    var repeticiones by remember { mutableStateOf(ejercicio.repeticiones) }
    var series by remember { mutableStateOf(ejercicio.series) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Editar Ejercicio", fontSize = 20.sp)

        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
        TextField(value = subcategoria, onValueChange = { subcategoria = it }, label = { Text("Subcategoría") })

        TextField(
            value = dias.joinToString(", "),
            onValueChange = { dias = it.split(",").map { s -> s.trim() } },
            label = { Text("Días (separados por coma)") }
        )

        OutlinedTextField(
            value = descanso.toString(),
            onValueChange = { descanso = it.toIntOrNull() ?: 0 },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = { Text("Descanso (s)") }
        )

        OutlinedTextField(
            value = repeticiones.toString(),
            onValueChange = { repeticiones = it.toIntOrNull() ?: 0 },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = { Text("Repeticiones") }
        )

        OutlinedTextField(
            value = series.toString(),
            onValueChange = { series = it.toIntOrNull() ?: 0 },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = { Text("Series") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.actualizarEjercicio(
                bloqueId = ejercicio.bloqueId ?: return@Button,
                rutinaKey = rutinaKey,
                ejercicioActual = ejercicio,
                nuevoNombre = nombre,
                nuevaCategoria = categoria,
                nuevaSubcategoria = subcategoria,
                nuevosDias = dias,
                nuevoDescanso = descanso,
                nuevasRepeticiones = repeticiones,
                nuevasSeries = series
            )
            navController.popBackStack()
        }) {
            Text("Guardar Cambios")
        }
    }
}
