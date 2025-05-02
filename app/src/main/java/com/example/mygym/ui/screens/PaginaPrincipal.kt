package com.example.mygym.ui.screens
//-- ↓ Imports ↓ -------------------------------------------------
import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import java.time.DayOfWeek
import java.time.LocalDate


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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(236, 240, 241)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//            HeaderPaginaPrincipal(navController, viewModel, viewModelCaracteristicas, dataUserViewModel, calendarViewModel)
        Spacer(modifier = Modifier.height(10.dp))
        CardDiasSemana(
            navController = navController,
            dias_semana = "Lunes"
        )
        CardDiasSemana(
            navController = navController,
            dias_semana = "Martes"
        )
        CardDiasSemana(
            navController = navController,
            dias_semana = "Miércoles"
        )
        CardDiasSemana(
            navController = navController,
            dias_semana = "Jueves"
        )
        CardDiasSemana(
            navController = navController,
            dias_semana = "Viernes"
        )
        CardDiasSemana(
            navController = navController,
            dias_semana = "Sábado"
        )
        CardDiasSemana(
            navController = navController,
            dias_semana = "Domingo"
        )
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(236, 240, 241)),
//            contentAlignment = Alignment.Center
//        ) {
//            when {
//                rutinasGuardadas.isNotEmpty() -> {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        contentPadding = PaddingValues(16.dp)
//                    ) {
//                        items(rutinasGuardadas) { rutina ->
//                            RutinaCard(rutina)
//                        }
//                    }
//                }
//
//                mostrarMensaje -> {
//                    Text(
//                        text = "No hay entrenamientos disponibles",
//                        color = Color.Black,
//                        fontSize = 18.sp
//                    )
//                }
//
//                else -> {
//                    CircularProgressIndicator(color = Color.Gray)
//                }
//            }
//        }
    }
}


@Composable
fun RutinaCard(rutina: DataClassCaracteristicasEntrenamientos) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = rutina.nombreEntrenamiento ?: "Entrenamiento sin nombre",
                style = TextStyle(fontSize = 20.sp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.padding(vertical = 4.dp))

            rutina.dias?.takeIf { it.isNotEmpty() }?.let {
                Text(
                    text = "📅 Días: ${it.joinToString(", ")}",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.DarkGray
                )
            }

            rutina.subcategorias?.takeIf { it.isNotEmpty() }?.let {
                Text(
                    text = "📦 Categorías: ${it.joinToString(", ")}",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.DarkGray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardDiasSemana(
    dias_semana: String,
    navController: NavController
) {
    val today = LocalDate.now().dayOfWeek

    val dayOfWeekSpanish = mapDayOfWeekToSpanish(today)

    val isToday = dias_semana.equals(dayOfWeekSpanish, ignoreCase = true)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, bottom = 10.dp)
            .height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dias_semana,
                color = Color.Black,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .padding(start = 8.dp)
            )

            Button(
                onClick = { navController.navigate("paginaIniciarEntrenamiento")},
                enabled = isToday,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(152, 242, 105),
                    disabledContainerColor = Color(224, 224, 224 ),
                    contentColor = Color.Black,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier
                    .height(48.dp)
            ) {
                Text("Iniciar", style = TextStyle(fontSize = 16.sp))
            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun mapDayOfWeekToSpanish(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lunes"
        DayOfWeek.TUESDAY -> "Martes"
        DayOfWeek.WEDNESDAY -> "Miércoles"
        DayOfWeek.THURSDAY -> "Jueves"
        DayOfWeek.FRIDAY -> "Viernes"
        DayOfWeek.SATURDAY -> "Sábado"
        DayOfWeek.SUNDAY -> "Domingo"
    }
}
