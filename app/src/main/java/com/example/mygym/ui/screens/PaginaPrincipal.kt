package com.example.mygym.ui.screens
//-- ↓ Imports ↓ -------------------------------------------------
import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel


//@RequiresApi(Build.VERSION_CODES.O)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaginaPrincipal(
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderPaginaPrincipal(navController,viewModel,viewModelCaracteristicas,dataUserViewModel,calendarViewModel)

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Btn_PaginaPrincipal(navController)
//                Btn_PaginaCrearEntrenamiento(navController)
//                Btn_Calendario(navController)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                                RutinaCard(rutina)
                            }
                        }
                    }

                    mostrarMensaje -> {
                        Text(
                            text = "No hay entrenamientos disponibles",
                            color = Color.Black,
                            fontSize = 18.sp
//                            fontWeight = FontWeight.Bold
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
fun RutinaCard(rutina: DataClassCaracteristicasEntrenamientos) {
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
            ){

                Text(
                    text = rutina.nombreEntrenamiento ?: "Nombre desconocido",
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = rutina.nombre ?: "Rutina desconocida",
                    color = Color.DarkGray,
                    style = TextStyle(fontSize = 17.sp)
                )
            }


            rutina.subcategorias?.forEach { subcategoria ->
                Text(
                    text = "• $subcategoria",
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
            }
        }
    }
}
