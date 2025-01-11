package com.example.mygym.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.unit.sp
import com.example.mygym.model.CategoriaEntrenamientos


@Composable
fun CrearEntrenamiento(
    navController: NavController,
    viewModel: MainViewModel,
) {
    var nombreInput by remember { mutableStateOf(viewModel.nombre) }
    var diaInput by remember { mutableStateOf(viewModel.dia) }
    var entrenamientoInput by remember { mutableStateOf(viewModel.entrenamiento) }
    var categoriaEntrenamientos = CategoriaEntrenamientos.values().toList()
    var categoriaEntrenameintoInput by remember {
        mutableStateOf<List<CategoriaEntrenamientos>>(
            emptyList()
        )
    }

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
                entrenamientoInput = entrenamientoInput,
                categoriaEntrenameintoInput = categoriaEntrenameintoInput
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
            onEntrenamientoChange = { entrenamientoInput = it },
            viewModel = viewModel,
            categoriaEntrenameintoInput = categoriaEntrenameintoInput,
            onCategoriaChange = { categoriaEntrenameintoInput = it }
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
    viewModel: MainViewModel,
    categoriaEntrenameintoInput: List<CategoriaEntrenamientos>,
    onCategoriaChange: (List<CategoriaEntrenamientos>) -> Unit,
) {
    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val diasAbreviados = listOf("L", "M", "X", "J", "V", "S", "D")
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
            value = entrenamientoInput,
            onValueChange = onEntrenamientoChange,
            label = { Text(text = "Entrenamiento") },
            shape = RoundedCornerShape(30.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "¿Qué día quieres entrenar?")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            diasAbreviados.forEachIndexed { index, abreviatura ->
                val isSelected = viewModel.dia == dias[index]
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color.Green else Color.White,
                        contentColor = Color.White
                    ),
                    onClick = { viewModel.dia = dias[index] },
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = abreviatura, fontSize = 12.sp, color = Color.Black)
                }
            }
        }
        //Text(
        //text = "Día seleccionado: ${viewModel.dia}",
        //fontSize = 16.sp,
        //modifier = Modifier.padding(top = 16.dp),
        //color = Color.White
        //)

        Text(text = "Elige tu categoría")
        var expanded by remember { mutableStateOf(false) }
        var categoriaEntrenameintoInput by remember {
            mutableStateOf<List<CategoriaEntrenamientos>>(
                emptyList()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                IconButton(onClick = { expanded = !expanded }) {
                    Text(text = "Gimnasio")

                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Gimnasio") },
                        onClick = {
                            categoriaEntrenameintoInput = listOf(CategoriaEntrenamientos.GIMNASIO)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cardio") },
                        onClick = {
                            categoriaEntrenameintoInput = listOf(CategoriaEntrenamientos.CARDIO)
                            expanded = false
                        }
                    )
                    //HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Fuerza") },
                        onClick = {
                            categoriaEntrenameintoInput = listOf(CategoriaEntrenamientos.FUERZA)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Flexibilidad") },
                        onClick = {
                            categoriaEntrenameintoInput =
                                listOf(CategoriaEntrenamientos.FLEXIBILIDAD)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Entrenamientos Mixtos") },
                        onClick = {
                            categoriaEntrenameintoInput =
                                listOf(CategoriaEntrenamientos.ENTRENAMIENTOSMIXTOS)
                            expanded = false
                        }
                    )

                }
            }

        }
    }
}

@Composable
fun BotonGuardar(
    navController: NavController,
    viewModel: MainViewModel,
    nombreInput: String,
    entrenamientoInput: String,
    categoriaEntrenameintoInput: List<CategoriaEntrenamientos>
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
                    val diaInput = viewModel.dia
                    if (nombreInput.isNotBlank() && diaInput.isNotBlank() && entrenamientoInput.isNotBlank()) {
                        val nuevoEntrenamiento = Entrenamientos(
                            id = 0,
                            nombre = nombreInput,
                            dia = diaInput,
                            entrenamiento = entrenamientoInput,
                            categoriaEntrenamientos = categoriaEntrenameintoInput

                        )

                        viewModel.entrenamientos = viewModel.entrenamientos + nuevoEntrenamiento
                        navController.navigate("paginaPrincipal")
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Guardar", color = Color.Black)
            }
        }
    }
}
