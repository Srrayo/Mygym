package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

    // Agrupamos las rutinas por bloqueId
    val rutinasPorBloque = rutinasGuardadas.groupBy { it.bloqueId ?: "Sin ID" }

    LaunchedEffect(rutinasGuardadas) {
        kotlinx.coroutines.delay(3000)
        if (rutinasGuardadas.isEmpty()) {
            mostrarMensaje = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(236, 240, 241)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        CrearNuevoEntreanmiento(navController)
        Spacer(modifier = Modifier.height(5.dp))
        Text("Editar entrenamientos")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(236, 240, 241)),
            contentAlignment = Alignment.Center
        ) {
            when {
                rutinasGuardadas.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        rutinasPorBloque.forEach { (bloqueId, rutinas) ->
                            item {
                                RutinaCardBloque(
                                    bloqueId = bloqueId,
                                    rutinas = rutinas,
                                    onClickEditar = {
                                        navController.navigate("editar_bloqueEntrenamiento/$bloqueId")
                                    }
                                )
                            }
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



@Composable
fun CrearNuevoEntreanmiento(navController: NavController) {
    Button(
        onClick = { navController.navigate("categoriaEntrenamientos") },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(244, 208, 63),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .shadow(10.dp, RoundedCornerShape(10.dp))
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
fun RutinaCardEntrenamiento(bloqueId: String, onClick: () -> Unit) {
    Log.d("Debug", "Bloque de entrenamiento: $bloqueId")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bloque: $bloqueId",  // Mostramos el ID del bloque
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}


@Composable
fun RutinaCard(rutina: DataClassCaracteristicasEntrenamientos, onClick: () -> Unit) {
    Log.d("Debug", "Rutina: $rutina, Categoria: ${rutina.categoria}, Nombre: ${rutina.nombreEntrenamiento}")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rutina.nombreEntrenamiento ?: "Nombre desconocido",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Categoría: ${rutina.categoria ?: "Desconocida"}",
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}


@Composable
fun RutinaCardBloque(
    bloqueId: String,
    rutinas: List<DataClassCaracteristicasEntrenamientos>,
    onClickEditar: () -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expandido = !expandido },
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Bloque: $bloqueId",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            if (expandido) {
                Spacer(modifier = Modifier.height(8.dp))
                rutinas.forEach { rutina ->
                    RutinaCard(rutina = rutina, onClick = { onClickEditar() })
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
