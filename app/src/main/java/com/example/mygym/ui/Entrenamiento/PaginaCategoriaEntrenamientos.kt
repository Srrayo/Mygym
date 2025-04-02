package com.example.mygym.ui.Entrenamiento

//-- ↓ Imports ↓ -------------------------------------------------

import CaracteristicasEntrenamientoViewModel
import android.util.Log
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
import com.example.mygym.model.CaracteristicasEntrenamientos
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
    var mostrarLista by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var subcategoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var nombreEntrenamiento by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
                Button(
                    onClick = {
                        if (categoriaSeleccionada != null && subcategoriaSeleccionada != null && nombreEntrenamiento.isNotEmpty()) {
                            viewModelCategoria.guardarRutinaEnFirestore(
                                userId, categoriaSeleccionada!!, subcategoriaSeleccionada!!, nombreEntrenamiento
                            )
                            Toast.makeText(
                                context,
                                "Entrenamiento guardado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("paginaPrincipal")
                        } else {
                            Toast.makeText(
                                context,
                                "Tienes que rellenar todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    modifier = Modifier
                        .width(150.dp)
                        .padding(vertical = 20.dp)
                        .height(45.dp),
                ) {
                    Text(text = "Guardar", color = Color.Black)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CerrarVentana(navController)
            TextField(
                value = nombreEntrenamiento,
                onValueChange = { nombreEntrenamiento = it },
                label = { Text(text = "Nombre del entrenamiento") },
                modifier = Modifier
                    .width(250.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // Define la acción del teclado
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // Cierra el teclado
                        focusManager.clearFocus() // Quita el foco del TextField (el cursor desaparece)
                    }
                )
            )
            Text(
                text = "Categoría: ${categoriaSeleccionada ?: "No seleccionada"}\nSubcategoría: ${subcategoriaSeleccionada ?: "No seleccionada"}\nNombre: ${nombreEntrenamiento.ifEmpty { "No ingresado" }}",
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
            Button(
                onClick = { mostrarLista = !mostrarLista },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
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
        }
    }
}


@Composable
fun EntrenamientosCardExpandible(
    caracteristicas: CaracteristicasEntrenamientos,
    onSeleccionar: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
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
