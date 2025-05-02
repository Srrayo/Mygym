package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.ui.Entrenamiento.ControlConBoton
import com.example.mygym.ui.Entrenamiento.EntrenamientosCardExpandible
import com.example.mygym.ui.Entrenamiento.SelectorDiasSemana


//@Composable
//fun PantallaEditarRutina(
//    navController: NavController,
//    viewModel: CaracteristicasEntrenamientoViewModel,
//    bloqueId: String,
//    rutinaKey: String,
//    nombreEntrenamiento: String,
//    categoria: String,
//    subcategorias: List<String>,
//    dias: List<String>,
//    descanso: Int,
//    series: Int,
//    repeticiones: Int
//) {
//    Log.d("PantallaEditarRutina", "bloqueId: $bloqueId, nombreEntrenamiento: $nombreEntrenamiento, categoria: $categoria")
//
//    var nombre by remember { mutableStateOf(nombreEntrenamiento) }
//    var categoriaState by remember { mutableStateOf(categoria) }
//    var subcategoria by remember { mutableStateOf(subcategorias.joinToString(", ")) }
//    var diasState by remember { mutableStateOf(dias.joinToString(", ")) }
//    var descansoState by remember { mutableStateOf(descanso) }
//    var repeticionesState by remember { mutableStateOf(repeticiones) }
//    var seriesState by remember { mutableStateOf(series) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        Text("Editar Rutina", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Nombre:")
//        TextField(
//            value = nombre,
//            onValueChange = { nombre = it },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Nombre de la rutina") }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Categoría:")
//        TextField(
//            value = categoriaState,
//            onValueChange = { categoriaState = it },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Categoría de la rutina") }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Subcategoría:")
//        TextField(
//            value = subcategoria,
//            onValueChange = { subcategoria = it },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Subcategoría de la rutina") }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Días:")
//        TextField(
//            value = diasState,
//            onValueChange = { diasState = it },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Días de la rutina") }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Descanso (segundos):")
//        TextField(
//            value = descansoState.toString(),
//            onValueChange = { descansoState = it.toIntOrNull() ?: 0 },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Tiempo de descanso") },
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Repeticiones:")
//        TextField(
//            value = repeticionesState.toString(),
//            onValueChange = { repeticionesState = it.toIntOrNull() ?: 0 },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Número de repeticiones") },
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Series:")
//        TextField(
//            value = seriesState.toString(),
//            onValueChange = { seriesState = it.toIntOrNull() ?: 0 },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("Número de series") },
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                val subcategoriasList = subcategoria.split(", ").filter { it.isNotBlank() } // Convierte el texto a lista
//                val diasList = diasState.split(", ").filter { it.isNotBlank() } // Convierte el texto a lista
//
//                val ejercicioActual = DataClassCaracteristicasEntrenamientos(
//                    nombreEntrenamiento = nombre,
//                    categoria = categoriaState,
//                    subcategorias = subcategoriasList,
//                    dias = diasList,
//                    descanso = descansoState,
//                    repeticiones = repeticionesState,
//                    series = seriesState,
//                    bloqueId = bloqueId,
//                    rutinaKey = rutinaKey
//                )
//                Log.d(
//                    "ActualizarEjercicioDebug", """
//    bloqueId: $bloqueId
//    rutinaKey: $rutinaKey
//    nombre: $nombre
//    categoria: $categoriaState
//    subcategoria: $subcategoria
//    dias: $diasList
//    descanso: $descansoState
//    repeticiones: $repeticionesState
//    series: $seriesState
//""".trimIndent()
//                )
//
//                viewModel.actualizarEjercicio(
//                    bloqueId = bloqueId,
//                    rutinaKey = rutinaKey,
//                    ejercicioActual = ejercicioActual,
//                    nuevoNombre = nombre,
//                    nuevaCategoria = categoriaState,
//                    nuevaSubcategoria = subcategoria,
//                    nuevosDias = diasList,
//                    nuevoDescanso = descansoState,
//                    nuevasRepeticiones = repeticionesState,
//                    nuevasSeries = seriesState
//                )
//
//                navController.popBackStack()
//            },
//            modifier = Modifier.fillMaxWidth().height(50.dp)
//        ) {
//            Text("Guardar cambios")
//        }
//
//    }
//}

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
    val caracteristicasEntrenamientos by viewModel.entrenamiento.collectAsStateWithLifecycle()
    var nombre by remember { mutableStateOf(nombreEntrenamiento) }
    var categoriaSeleccionada by remember { mutableStateOf(categoria) }
    var subcategoriaSeleccionada by remember { mutableStateOf(subcategorias.joinToString(", ")) }
    var diasState by remember { mutableStateOf(dias.joinToString(", ")) }
    var descansoState by remember { mutableStateOf(descanso) }
    var repeticionesState by remember { mutableStateOf(repeticiones) }
    var seriesState by remember { mutableStateOf(series) }
    var mostrarLista by remember { mutableStateOf(false) }
    var showSeriesInput by remember { mutableStateOf(false) }
    var showRepeticionesInput by remember { mutableStateOf(false) }
    var showDescansoInput by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(Color(44, 44, 44))
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(44, 44, 44),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        val subcategoriasList = subcategoriaSeleccionada.split(", ").filter { it.isNotBlank() }
                        val diasList = diasState.split(", ").filter { it.isNotBlank() }

                        viewModel.actualizarEjercicio(
                            bloqueId = bloqueId,
                            rutinaKey = rutinaKey,
                            ejercicioActual = DataClassCaracteristicasEntrenamientos(
                                nombreEntrenamiento = nombre,
                                categoria = categoriaSeleccionada,
                                subcategorias = subcategoriasList,
                                dias = diasList,
                                descanso = descansoState,
                                repeticiones = repeticionesState,
                                series = seriesState,
                                bloqueId = bloqueId,
                                rutinaKey = rutinaKey
                            ),
                            nuevoNombre = nombre,
                            nuevaCategoria = categoriaSeleccionada,
                            nuevaSubcategoria = subcategoriaSeleccionada,
                            nuevosDias = diasList,
                            nuevoDescanso = descansoState,
                            nuevasRepeticiones = repeticionesState,
                            nuevasSeries = seriesState,
                        )
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(244, 208, 63),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(236, 240, 241))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Editar Entrenamiento",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del entrenamiento") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar la categoría y subcategoría actual seleccionada
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Categoría actual:",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = categoriaSeleccionada,
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Subcategoría actual:",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = subcategoriaSeleccionada,
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { mostrarLista = !mostrarLista },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(95, 95, 95),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp)
                ) {
                    Text(text = if (mostrarLista) "Ocultar Categorías" else "Categoría")
                }
            }

            if (mostrarLista) {
                items(caracteristicasEntrenamientos) { entrenamiento ->
                    EntrenamientosCardExpandible(
                        caracteristicas = entrenamiento,
                        onSeleccionar = { categoria, subcategoria ->
                            categoriaSeleccionada = categoria
                            subcategoriaSeleccionada = subcategoria
                            mostrarLista = false
                        }
                    )
                }
            }

            item {
                ControlConBoton(
                    caracteristica = "series",
                    titulo = "Número de series",
                    valor = seriesState,
                    onValueChange = { seriesState = it },
                    showInput = showSeriesInput,
                    onToggleInput = { showSeriesInput = !showSeriesInput }
                )
            }

            item {
                ControlConBoton(
                    caracteristica = "repeticiones",
                    titulo = "Número de repeticiones",
                    valor = repeticionesState,
                    onValueChange = { repeticionesState = it },
                    showInput = showRepeticionesInput,
                    onToggleInput = { showRepeticionesInput = !showRepeticionesInput }
                )
            }

            item {
                ControlConBoton(
                    caracteristica = "segundos",
                    titulo = "Descanso entre series",
                    valor = descansoState,
                    onValueChange = { descansoState = it },
                    showInput = showDescansoInput,
                    onToggleInput = { showDescansoInput = !showDescansoInput }
                )
            }

            item {
                SelectorDiasSemana(
                    diasIniciales = diasState.split(", ").filter { it.isNotBlank() }.toSet(),
                    onDiasSeleccionadosChange = { nuevosDias ->
                        diasState = nuevosDias.joinToString(", ")
                    }
                )
            }
        }
    }
}