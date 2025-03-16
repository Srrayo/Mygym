package com.example.mygym.ui.Entrenamiento

import CaracteristicasEntrenamientoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mygym.model.CaracteristicasEntrenamientos

@Composable
fun PaginaCategoriaEntrenamientos(
    viewModelCategoria: CaracteristicasEntrenamientoViewModel,
    navController: NavController
) {
    val caracteristicasEntrenamientos: State<List<CaracteristicasEntrenamientos>> =
        viewModelCategoria.entrenamiento.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        RutinasGuardadasScreen(viewModelCategoria)
        LazyColumn {
            items(caracteristicasEntrenamientos.value) { entrenamiento ->
                EntrenamientosCard(entrenamiento)
            }
        }
    }
}

@Composable
fun EntrenamientosCard(caracteristicas: CaracteristicasEntrenamientos) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(onClick = { expanded = !expanded }) {
                Text(text = caracteristicas.nombre, color = Color.White)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                caracteristicas.subcategorias.forEach { subcategoria ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(text = subcategoria, color = Color.Black) },
                        onClick = { expanded = false }
                    )

                }
            }

        }
    }
}

@Composable
fun EntrenamientosItem(caracteristicas: CaracteristicasEntrenamientos) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = caracteristicas.nombre.orEmpty(), color = Color.White)
        Text(text = caracteristicas.subcategorias.joinToString(", "), color = Color.White)
    }
}

@Composable
fun CerrarVentana(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { navController.navigate("paginaCrearEntrenamiento") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Red
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "volver",
                modifier = Modifier.size(20.dp)
            )
        }
        Text(text = "Nuevo entrenamiento")
    }
}

@Composable
fun RutinasGuardadasScreen(viewModel: CaracteristicasEntrenamientoViewModel) {
    // Obtener las rutinas guardadas del ViewModel
    val rutinasGuardadas by viewModel.rutinasGuardadas.collectAsState()

    // Mostrar un indicador de carga si las rutinas están vacías
    if (rutinasGuardadas.isEmpty()) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(rutinasGuardadas) { rutina ->
                Text("Nombre de la rutina: ${rutina.nombre}")
                // Agregar otros campos de la clase CaracteristicasEntrenamientos aquí
            }
        }
    }
}


/**
 * fun createEntrenamientos(db: FirebaseFirestore) {
 *     val entrenamiento = Entrenamiento("GYM")
 *     db.collection("Categorias")
 *         .add(entrenamiento)
 *         .addOnSuccessListener { Log.i("Entrenamiento", "SUCCESS") }
 *         .addOnCompleteListener { Log.i("Entrenamiento", "COMPLETE") }
 *         .addOnFailureListener { Log.i("Entrenamiento", "FAILURE") }
 */
