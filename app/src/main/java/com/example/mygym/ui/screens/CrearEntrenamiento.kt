package com.example.mygym.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mygym.model.Entrenamientos
import com.example.mygym.model.MainViewModel
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField


@Composable
fun CrearEntrenamiento(
    navController: NavController,
    viewModel: MainViewModel,
) {
    var nombreInput by remember { mutableStateOf(viewModel.nombre) }
    var diaInput by remember { mutableStateOf(viewModel.dia) }
    var entrenamientoInput by remember { mutableStateOf(viewModel.entrenamiento) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CerrarVentana(navController)
        },
        bottomBar = {
            BotonGuardar(
                navController = navController,
                viewModel = viewModel,
                nombreInput = nombreInput,
                diaInput = diaInput,
                entrenamientoInput = entrenamientoInput
            )
        },
    ) { innerPadding ->
        RellenarCampos(
            innerPadding = innerPadding,
            nombreInput = nombreInput,
            onNombreChange = { nombreInput = it },
            diaInput = diaInput,
            onDiaChange = { diaInput = it },
            entrenamientoInput = entrenamientoInput,
            onEntrenamientoChange = { entrenamientoInput = it }
        )
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
        Text(text = "Nuevo entranamiento")
    }
}


@Composable
fun RellenarCampos(
    innerPadding: PaddingValues,
    nombreInput: String,
    onNombreChange: (String) -> Unit,
    diaInput: String,
    onDiaChange: (String) -> Unit,
    entrenamientoInput: String,
    onEntrenamientoChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nombreInput,
            onValueChange = onNombreChange,
            label = { Text(text = "Nombre") },
            shape = RoundedCornerShape(30.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = diaInput,
            onValueChange = onDiaChange,
            label = { Text(text = "Día") },
            shape = RoundedCornerShape(30.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = entrenamientoInput,
            onValueChange = onEntrenamientoChange,
            label = { Text(text = "Entrenamiento") },
            shape = RoundedCornerShape(30.dp)
        )
    }
}

@Composable
fun BotonGuardar(
    navController: NavController,
    viewModel: MainViewModel,
    nombreInput: String,
    diaInput: String,
    entrenamientoInput: String,
) {
    BottomAppBar {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = {

                    if (nombreInput.isNotBlank() && diaInput.isNotBlank() && entrenamientoInput.isNotBlank()) {
                        val nuevoEntrenamiento = Entrenamientos(
                            id = "",
                            nombre = nombreInput,
                            dia = diaInput,
                            entrenamiento = entrenamientoInput
                        )
                        viewModel.entrenamientos = viewModel.entrenamientos + nuevoEntrenamiento
                        navController.navigate("paginaPrincipal")
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Guardar",color = Color.Black)
            }
        }
    }
}
