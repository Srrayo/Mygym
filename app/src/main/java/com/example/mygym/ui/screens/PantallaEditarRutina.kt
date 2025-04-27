package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos


@Composable
fun PantallaEditarRutina(
    navController: NavController,
    viewModel: CaracteristicasEntrenamientoViewModel,
    bloqueId: String,
    rutinaKey: String,
    nombreEntrenamiento: String,
    categoria: String,
    subcategorias: List<String>,
    dias: List<String>,
    descanso: Int,
    series: Int,
    repeticiones: Int
) {
    Log.d("PantallaEditarRutina", "bloqueId: $bloqueId, nombreEntrenamiento: $nombreEntrenamiento, categoria: $categoria")

    var nombre by remember { mutableStateOf(nombreEntrenamiento) }
    var categoriaState by remember { mutableStateOf(categoria) }
    var subcategoria by remember { mutableStateOf(subcategorias.joinToString(", ")) }
    var diasState by remember { mutableStateOf(dias.joinToString(", ")) }
    var descansoState by remember { mutableStateOf(descanso) }
    var repeticionesState by remember { mutableStateOf(repeticiones) }
    var seriesState by remember { mutableStateOf(series) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Editar Rutina", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nombre:")
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Nombre de la rutina") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Categoría:")
        TextField(
            value = categoriaState,
            onValueChange = { categoriaState = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Categoría de la rutina") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Subcategoría:")
        TextField(
            value = subcategoria,
            onValueChange = { subcategoria = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Subcategoría de la rutina") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Días:")
        TextField(
            value = diasState,
            onValueChange = { diasState = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Días de la rutina") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Descanso (segundos):")
        TextField(
            value = descansoState.toString(),
            onValueChange = { descansoState = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tiempo de descanso") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Repeticiones:")
        TextField(
            value = repeticionesState.toString(),
            onValueChange = { repeticionesState = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Número de repeticiones") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Series:")
        TextField(
            value = seriesState.toString(),
            onValueChange = { seriesState = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Número de series") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val subcategoriasList = subcategoria.split(", ").filter { it.isNotBlank() } // Convierte el texto a lista
                val diasList = diasState.split(", ").filter { it.isNotBlank() } // Convierte el texto a lista

                val ejercicioActual = DataClassCaracteristicasEntrenamientos(
                    nombreEntrenamiento = nombre,
                    categoria = categoriaState,
                    subcategorias = subcategoriasList,
                    dias = diasList,
                    descanso = descansoState,
                    repeticiones = repeticionesState,
                    series = seriesState,
                    bloqueId = bloqueId,
                    rutinaKey = rutinaKey
                )
                Log.d(
                    "ActualizarEjercicioDebug", """
    bloqueId: $bloqueId
    rutinaKey: $rutinaKey
    nombre: $nombre
    categoria: $categoriaState
    subcategoria: $subcategoria
    dias: $diasList
    descanso: $descansoState
    repeticiones: $repeticionesState
    series: $seriesState
""".trimIndent()
                )

                viewModel.actualizarEjercicio(
                    bloqueId = bloqueId,
                    rutinaKey = rutinaKey,
                    ejercicioActual = ejercicioActual,
                    nuevoNombre = nombre,
                    nuevaCategoria = categoriaState,
                    nuevaSubcategoria = subcategoria,
                    nuevosDias = diasList,
                    nuevoDescanso = descansoState,
                    nuevasRepeticiones = repeticionesState,
                    nuevasSeries = seriesState
                )

                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Guardar cambios")
        }

    }
}
