package com.example.mygym.ui.EditarEntrenamiento

import CaracteristicasEntrenamientoViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.ui.screens.RutinaCardEntrenamiento

@Composable
fun PantallaEdicionEjercicio(
    ejercicio: DataClassCaracteristicasEntrenamientos,
    viewModel: CaracteristicasEntrenamientoViewModel,
    navController: NavController,
    rutinaKey: String // "rutina_0", "rutina_1", etc.
) {
    var nuevoNombreEntrenamiento by remember { mutableStateOf(ejercicio.nombreEntrenamiento ?: "") }
    var nuevaCategoria by remember { mutableStateOf(ejercicio.categoria ?: "") }
    var nuevaSubcategoria by remember { mutableStateOf(ejercicio.subcategorias?.firstOrNull() ?: "") }
    var nuevosDias by remember { mutableStateOf(ejercicio.dias ?: emptyList()) }
    var nuevoDescanso by remember { mutableStateOf(ejercicio.descanso) }
    var nuevasRepeticiones by remember { mutableStateOf(ejercicio.repeticiones) }
    var nuevasSeries by remember { mutableStateOf(ejercicio.series) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Editar Ejercicio", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Nombre del ejercicio")
        TextField(value = nuevoNombreEntrenamiento, onValueChange = { nuevoNombreEntrenamiento = it })

        Text("Categoría")
        TextField(value = nuevaCategoria, onValueChange = { nuevaCategoria = it })

        Text("Subcategoría")
        TextField(value = nuevaSubcategoria, onValueChange = { nuevaSubcategoria = it })

        Text("Días (separados por coma)")
        TextField(
            value = nuevosDias.joinToString(", "),
            onValueChange = { nuevosDias = it.split(",").map { dia -> dia.trim() } }
        )

        Text("Descanso (segundos)")
        OutlinedTextField(
            value = if (nuevoDescanso == 0) "" else nuevoDescanso.toString(),
            onValueChange = { nuevoDescanso = it.toIntOrNull() ?: 0 },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Text("Repeticiones")
        OutlinedTextField(
            value = if (nuevasRepeticiones == 0) "" else nuevasRepeticiones.toString(),
            onValueChange = { nuevasRepeticiones = it.toIntOrNull() ?: 0 },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Text("Series")
        OutlinedTextField(
            value = if (nuevasSeries == 0) "" else nuevasSeries.toString(),
            onValueChange = { nuevasSeries = it.toIntOrNull() ?: 0 },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.actualizarEjercicio(
                bloqueId = ejercicio.bloqueId ?: return@Button,
                rutinaKey = rutinaKey,
                ejercicioActual = ejercicio,
                nuevoNombre = nuevoNombreEntrenamiento,
                nuevaCategoria = nuevaCategoria,
                nuevaSubcategoria = nuevaSubcategoria,
                nuevosDias = nuevosDias,
                nuevoDescanso = nuevoDescanso,
                nuevasRepeticiones = nuevasRepeticiones,
                nuevasSeries = nuevasSeries
            )
            navController.popBackStack()
        }) {
            Text("Guardar Cambios")
        }
    }
}
