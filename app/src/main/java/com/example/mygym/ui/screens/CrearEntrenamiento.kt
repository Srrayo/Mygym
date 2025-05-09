package com.example.mygym.ui.screens
//-- ↓ Imports ↓ -------------------------------------------------
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.model.MainViewModel
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp

//-- ↑ Imports ↑ -------------------------------------------------



@Composable
fun CrearEntrenamiento(
    navController: NavController,
    viewModel: MainViewModel,
    caracteristicas: DataClassCaracteristicasEntrenamientos
) {
    var nombreInput by remember { mutableStateOf(viewModel.nombre) }
    var diaInput by remember { mutableStateOf(viewModel.dia) }
    var entrenamientoInput by remember { mutableStateOf(viewModel.entrenamiento) }
    val mensajeError by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CerrarVentana(navController)
        },
        bottomBar = {
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
            mensajeError = mensajeError,
            caracteristicas =  caracteristicas
        )


    }
}



@OptIn(ExperimentalMaterial3Api::class)
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
    mensajeError: String,
    caracteristicas: DataClassCaracteristicasEntrenamientos
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
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        TextField(
            value = nombreInput,
            onValueChange = onNombreChange,
            label = { Text(text = "Nombre") },
            modifier = Modifier
                .width(100.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { expanded = !expanded }) {
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    /**
                     * CategoriaEntrenamientos.values().forEach { categoria ->
                     *                         DropdownMenuItem(
                     *                             text = { Text(categoria.name) },
                     *                             onClick = {
                     *                                 onCategoriaChange(categoria)
                     *                                 expanded = false
                     *                             }
                     *                         )
                     *                     }
                     */

                }
            }
        }

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


        if (mensajeError.isNotEmpty()) {
            Text(
                text = mensajeError,
                color = Color.Red,
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp
            )
        }

       // EntrenamientosCard(caracteristicas, navController)
    }
}
@Composable
fun BotonGuardar(
    navController: NavController,
    viewModel: MainViewModel,
    nombreInput: String,
    mensajeError: String,
    onMensajeErrorChange: (String) -> Unit
) {
    BottomAppBar {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = {
                    val diaInput = viewModel.dia
                    if (nombreInput.isNotBlank() && diaInput.isNotBlank()) {
                       // val nuevoEntrenamiento = CaracteristicasEntrenamientos(
                            //id = 0,
                            //nombre = nombreInput,
                            //dia = diaInput,
                            //categoriaEntrenamientos = listOf(categoriaEntrenameintoInput)
                       // )
                        //viewModel.entrenamientos = viewModel.entrenamientos + nuevoEntrenamiento
                        //navController.navigate("paginaPrincipal")
                    } else {
                        onMensajeErrorChange("Por favor, rellene todos los campos antes de guardar.")
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
