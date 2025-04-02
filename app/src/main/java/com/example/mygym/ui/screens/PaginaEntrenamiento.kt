package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.CaracteristicasEntrenamientos
import com.example.mygym.model.MainViewModel

@Composable
fun PaginaEntrenamiento(navController: NavController, viewModel: MainViewModel, calendarViewModel: CalendarViewModel) {

    MenuLateral(navController) { innerPadding ->
        // Apply vertical scroll to the Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()), // Enable scroll here
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Header(navController, viewModel)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Btn_PaginaPrincipal(navController)
                Btn_PaginaCrearEntrenamiento(navController)
                Btn_Calendario(navController)
            }

            Spacer(modifier = Modifier.height(16.dp))

            CrearNuevoEntreanmiento(navController, viewModel)

        }
    }
}


@Composable
fun CrearNuevoEntreanmiento(navController: NavController, viewModel: MainViewModel) {
    val entrenamientos = viewModel.entrenamientos
    var selectedDate by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .padding(30.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { navController.navigate("categoriaEntrenamientos") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.Black
                ),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "mas",
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
            }

        }
//        Calendario(
//            onDateSelected = { date ->
//                selectedDate = date
//            }
//        )
//        LazyEntrenamiento2(entrenamientos = entrenamientos, navController)

    }
}

@Composable
fun LazyEntrenamiento2(
    entrenamientos: List<CaracteristicasEntrenamientos>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(entrenamientos) { entrenamiento ->
            CardEntrenamientos2(entrenamiento = entrenamiento, navController)
        }
    }
}

@Composable
fun CardEntrenamientos2(
    entrenamiento: CaracteristicasEntrenamientos,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            /**
             * Text(
             *                 text = entrenamiento.nombre,
             *                 style = TextStyle(fontSize = 15.sp, color = Color.White),
             *                 modifier = Modifier.padding(start = 16.dp)
             *             )
             */

            Button(
                modifier = Modifier.padding(end = 15.dp),
                onClick = { navController.navigate("edicionEntrenamiento") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Create,
                    contentDescription = "moreVert",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White,
                )
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendario(
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Título del calendario
        Text(
            text = "Selecciona una fecha",
            style = MaterialTheme.typography.titleMedium
        )

        // DatePicker mostrando el calendario
        DatePicker(
            state = datePickerState,
            headline = {
                Text("Fecha seleccionada: ${datePickerState.selectedDateMillis?.let { formatDate(it) } ?: "Ninguna"}")
            },
            showModeToggle = true // Permite cambiar entre la vista de calendario y entrada manual
        )

        // Botón para confirmar la selección de fecha
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val selectedDateMillis = datePickerState.selectedDateMillis
                selectedDateMillis?.let {
                    onDateSelected(formatDate(it))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar")
        }
    }
}


