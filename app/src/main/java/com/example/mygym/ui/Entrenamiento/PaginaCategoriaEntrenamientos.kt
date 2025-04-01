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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                        navController.navigate("paginaPrincipal")
                        if (categoriaSeleccionada != null && subcategoriaSeleccionada != null) {
                            viewModelCategoria.guardarRutinaEnFirestore(
                                userId, categoriaSeleccionada!!, subcategoriaSeleccionada!!
                            )
                            Toast.makeText(
                                context,
                                "Entrenamiento guardado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Toast.makeText(
                                context,
                                "Tienes que rellenar todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    modifier = Modifier
                        .width(180.dp)
                        .padding(vertical = 20.dp)
                        .height(45.dp),

                    ) {
                    Text(text = "Guardar", color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues) // Evita que el contenido se solape con la barra inferior
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CerrarVentana(navController)

            Button(
                onClick = { mostrarLista = !mostrarLista },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Categorías")
            }

            Text(
                text = "Categoría: ${categoriaSeleccionada ?: "No seleccionada"}\nSubcategoría: ${subcategoriaSeleccionada ?: "No seleccionada"}",
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )

            if (caracteristicasEntrenamientos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
//                    CircularProgressIndicator()
                }
            } else if (mostrarLista) {
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

            RutinasGuardadasScreen(viewModelCategoria)
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
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(
                    text = caracteristicas.nombre ?: "Nombre desconocido",
                    color = Color.White
                )
            }

            if (expanded) {
                caracteristicas.subcategorias?.forEach { subcategoria ->
                    Text(
                        text = "$subcategoria",
                        color = Color.LightGray,
                        modifier = Modifier
                            .clickable {
                                onSeleccionar(caracteristicas.nombre ?: "Desconocido", subcategoria)
                            }
                            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun RutinasGuardadasScreen(viewModel: CaracteristicasEntrenamientoViewModel) {
    val rutinasGuardadas by viewModel.rutinasGuardadas.collectAsState()

    if (rutinasGuardadas.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
//            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn {
            items(rutinasGuardadas) { rutina ->
                Text("Nombre de la rutina: ${rutina.nombre}", color = Color.White)
            }
        }
    }
}
