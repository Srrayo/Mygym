package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaginaEntrenamiento(
    navController: NavController,
    viewModel: MainViewModel,
    viewModelCaracteristicas: CaracteristicasEntrenamientoViewModel,
    dataUserViewModel: DataUserViewModel,
    calendarViewModel: CalendarViewModel
) {
    val rutinasGuardadas by viewModelCaracteristicas.rutinasGuardadas.collectAsState()
    var mostrarMensaje by remember { mutableStateOf(false) }
    LaunchedEffect(rutinasGuardadas) {
        kotlinx.coroutines.delay(3000)
        if (rutinasGuardadas.isEmpty()) {
            mostrarMensaje = true
        }
    }
    MenuLateral(navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderPaginaPrincipal(
                navController,
                viewModel,
                viewModelCaracteristicas,
                dataUserViewModel,
                calendarViewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
            CrearNuevoEntreanmiento(navController)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {

                when {
                    rutinasGuardadas.isNotEmpty() -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(rutinasGuardadas) { rutina ->
                                RutinaCardEntrenamiento(rutina)
                            }
                        }
                    }

                    mostrarMensaje -> {
                        Text(
                            text = "No hay entrenamientos disponibles",
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    }

                    else -> {
                        CircularProgressIndicator(color = Color.Gray)
                    }
                }
            }
        }
    }
}


@Composable
fun CrearNuevoEntreanmiento(navController: NavController) {

    Button(
        onClick = { navController.navigate("categoriaEntrenamientos") },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50), // Verde brillante
            contentColor = Color.White // Color blanco para el texto
        ),
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(50.dp)) // Forma de píldora para bordes más suaves
            .shadow(4.dp, RoundedCornerShape(50.dp)), // Sombra para darle profundidad
        shape = RoundedCornerShape(50.dp) // Píldora
    ) {
        Text(
            text = "Crea un nuevo entrenamiento",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}


@Composable
fun LazyEntrenamiento2(
    entrenamientos: List<DataClassCaracteristicasEntrenamientos>,
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
    entrenamiento: DataClassCaracteristicasEntrenamientos,
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


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Calendario(
//    onDateSelected: (String) -> Unit
//) {
//    val datePickerState = rememberDatePickerState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        // Título del calendario
//        Text(
//            text = "Selecciona una fecha",
//            style = MaterialTheme.typography.titleMedium
//        )
//
//        // DatePicker mostrando el calendario
//        DatePicker(
//            state = datePickerState,
//            headline = {
//                Text("Fecha seleccionada: ${datePickerState.selectedDateMillis?.let { formatDate(it) } ?: "Ninguna"}")
//            },
//            showModeToggle = true // Permite cambiar entre la vista de calendario y entrada manual
//        )
//
//        // Botón para confirmar la selección de fecha
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                val selectedDateMillis = datePickerState.selectedDateMillis
//                selectedDateMillis?.let {
//                    onDateSelected(formatDate(it))
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Confirmar")
//        }
//    }
//}


@Composable
fun RutinaCardEntrenamiento(rutina: DataClassCaracteristicasEntrenamientos) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            // horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = rutina.nombreEntrenamiento ?: "Nombre desconocido",
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = rutina.nombreEntrenamiento ?: "Rutina desconocida",
                    color = Color.DarkGray,
                    style = TextStyle(fontSize = 17.sp)
                )
            }


//            rutina.subcategorias?.forEach { subcategoria ->
//                Text(
//                    text = "• $subcategoria",
//                    color = Color.DarkGray,
//                    fontSize = 16.sp
//                )
//            }
        }
    }
}


