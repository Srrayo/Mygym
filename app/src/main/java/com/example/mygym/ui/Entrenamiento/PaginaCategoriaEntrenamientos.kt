package com.example.mygym.ui.Entrenamiento

//-- ↓ Imports ↓ -------------------------------------------------

import CaracteristicasEntrenamientoViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.ui.screens.CerrarVentana

//-- ↑ Imports ↑ -------------------------------------------------

@Composable
fun PaginaCategoriaEntrenamientos(
    viewModelCategoria: CaracteristicasEntrenamientoViewModel,
    navController: NavController,
    userId: String
) {
    val caracteristicasEntrenamientos by viewModelCategoria.entrenamiento.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var mostrarLista by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var subcategoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var nombreEntrenamiento by remember { mutableStateOf("") }
    var diasSeleccionados by remember { mutableStateOf(setOf<String>()) }

    val listaEjercicios = remember { mutableStateListOf<Map<String, Any>>() }
    val bloqueTimestamp = remember { System.currentTimeMillis() }

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModelCategoria.getEntrenamiento(userId)
        }
    }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    if (categoriaSeleccionada != null && subcategoriaSeleccionada != null) {
                        val nuevoEjercicio = mapOf(
                            "nombreEntrenamiento" to nombreEntrenamiento,
                            "categoria" to categoriaSeleccionada!!,
                            "subcategoria" to subcategoriaSeleccionada!!,
                            "dias" to diasSeleccionados.toList()
                        )
                        listaEjercicios.add(nuevoEjercicio)

                        // Limpiar solo categoría y subcategoría
                        categoriaSeleccionada = null
                        subcategoriaSeleccionada = null

                        Toast.makeText(context, "Ejercicio añadido", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Selecciona categoría y subcategoría", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Añadir nuevo")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    if (listaEjercicios.isNotEmpty()) {
                        listaEjercicios.forEachIndexed { index, rutina ->
                            val categoria = rutina["categoria"] as String
                            val subcategoria = rutina["subcategoria"] as String

                            viewModelCategoria.guardarRutinaEnFirestore(
                                userId,
                                categoria,
                                subcategoria,
                                nombreEntrenamiento,
                                diasSeleccionados,
                                bloqueTimestamp,
                                index
                            )
                        }

                        Toast.makeText(context, "Rutinas guardadas en Firebase", Toast.LENGTH_LONG).show()
                        navController.navigate("tabRowPantallas")
                    } else {
                        Toast.makeText(context, "No hay rutinas para guardar", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Guardar en Firebase")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CerrarVentana(navController)

            TextField(
                value = nombreEntrenamiento,
                onValueChange = {
                    nombreEntrenamiento = it
                    // Sincroniza nombre con todos los ejercicios en lista
                    listaEjercicios.replaceAll { ejercicio ->
                        ejercicio.toMutableMap().apply { this["nombreEntrenamiento"] = it }
                    }
                },
                label = { Text(text = "Nombre del entrenamiento") },
                modifier = Modifier.width(250.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black
                )
            )

            Text(
                text = "Categoría: ${categoriaSeleccionada ?: "No seleccionada"}\nSubcategoría: ${subcategoriaSeleccionada ?: "No seleccionada"}",
                color = Color.Black,
                modifier = Modifier.padding(10.dp)
            )

            Button(
                onClick = { mostrarLista = !mostrarLista },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(text = "Categorías")
            }

            if (caracteristicasEntrenamientos.isNotEmpty() && mostrarLista) {
                LazyColumn {
                    items(caracteristicasEntrenamientos) { entrenamiento ->
                        EntrenamientosCardExpandible(
                            caracteristicas = entrenamiento,
                            onSeleccionar = { categoria, subcategoria ->
                                categoriaSeleccionada = categoria
                                subcategoriaSeleccionada = subcategoria
                            }
                        )
                    }
                }
            }

            SelectorDiasSemana(
                diasIniciales = diasSeleccionados,
                onDiasSeleccionadosChange = { nuevosDias ->
                    diasSeleccionados = nuevosDias
                    listaEjercicios.replaceAll { ejercicio ->
                        ejercicio.toMutableMap().apply { this["dias"] = nuevosDias.toList() }
                    }
                }
            )

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            Text("Rutinas archivadas:", color = Color.Black)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1f)
            ) {
                items(listaEjercicios) { rutina ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Nombre: ${rutina["nombreEntrenamiento"]}")
                            Text("Categoría: ${rutina["categoria"]}")
                            Text("Subcategoría: ${rutina["subcategoria"]}")
                            Text("Días: ${(rutina["dias"] as List<String>).joinToString(", ")}")
                        }
                    }
                }
            }
        }
    }
} // Fin PaginaCategoriaEntrenamientos

@Composable
fun EntrenamientosCardExpandible(
    caracteristicas: DataClassCaracteristicasEntrenamientos,
    onSeleccionar: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(containerColor = Color(130, 168, 199)),
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(
                    text = caracteristicas.nombre ?: "Nombre desconocido",
                    color = Color.Black
                )
            }

            if (expanded) {
                caracteristicas.subcategorias?.forEach { subcategoria ->
                    Button(
                        onClick = {
                            onSeleccionar(caracteristicas.nombre ?: "Desconocido", subcategoria)
                            expanded = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(193, 221, 245)),
                        shape = RoundedCornerShape(3.dp)
                    ) {
                        Text(
                            text = subcategoria,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectorDiasSemana(
    diasIniciales: Set<String> = emptySet(),
    onDiasSeleccionadosChange: (Set<String>) -> Unit
) {
    val diasSemana = listOf("L", "M", "X", "J", "V", "S", "D")
    var diasSeleccionados by remember { mutableStateOf(diasIniciales) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Selecciona los días para este entrenamiento:",
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            diasSemana.forEach { dia ->
                val estaSeleccionado = diasSeleccionados.contains(dia)

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = if (estaSeleccionado) Color(0xFF4CAF50) else Color.Gray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            diasSeleccionados = if (estaSeleccionado)
                                diasSeleccionados - dia
                            else
                                diasSeleccionados + dia

                            onDiasSeleccionadosChange(diasSeleccionados)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dia,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
