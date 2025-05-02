package com.example.mygym.ui.IniciarEntrenamiento

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.R
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


enum class EstadoRutina {
    ACTIVA, PENDIENTE, COMPLETADA
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaginaIniciarEntrenamiento(
    navController: NavController,
    viewModelCaracteristicas: CalendarViewModel
) {
    val rutinasGuardadas by viewModelCaracteristicas.rutinasGuardadas.collectAsState()
    val diaActual = LocalDate.now()
        .dayOfWeek
        .getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }

    var rutinaActivaIndex by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    val showCancelDialog = remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (!isPaused && !showDialog) elapsedTime++
        }
    }

    val formattedTime = String.format("%02d:%02d:%02d", elapsedTime / 3600, (elapsedTime % 3600) / 60, elapsedTime % 60)

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("\u00a1Entrenamiento finalizado!") },
            text = { Text("Has completado todas tus rutinas del d\u00eda.") },
            confirmButton = {}
        )

        LaunchedEffect(Unit) {
            delay(2000)
            navController.navigate("tabRowPantallas") {
                popUpTo("paginaIniciarEntrenamiento") { inclusive = true }
            }
        }
    }

    if (showCancelDialog.value) {
        AlertDialog(
            onDismissRequest = { showCancelDialog.value = false },
            title = { Text("\u00bfCancelar entrenamiento?") },
            text = { Text("Se perder\u00e1 el progreso actual. \u00bfEst\u00e1s seguro?") },
            confirmButton = {
                Button(onClick = {
                    showCancelDialog.value = false
                    navController.navigate("tabRowPantallas") {
                        popUpTo("paginaIniciarEntrenamiento") { inclusive = true }
                    }
                }) {
                    Text("S\u00ed")
                }
            },
            dismissButton = {
                Button(onClick = { showCancelDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logob),
            contentDescription = "logo",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = diaActual,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = formattedTime, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (rutinasGuardadas.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(rutinasGuardadas.indices.toList()) { index ->
                        val estado = when {
                            index < rutinaActivaIndex -> EstadoRutina.COMPLETADA
                            index == rutinaActivaIndex -> EstadoRutina.ACTIVA
                            else -> EstadoRutina.PENDIENTE
                        }

                        RutinasDelDiaCard(
                            rutina = rutinasGuardadas[index],
                            estado = estado,
                            isPaused = isPaused,
                            onRutinaTerminada = {
                                if (rutinaActivaIndex + 1 >= rutinasGuardadas.size) {
                                    showDialog = true
                                } else {
                                    rutinaActivaIndex++
                                }
                            }
                        )
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { isPaused = !isPaused }, colors = ButtonDefaults.buttonColors(containerColor = Color(255, 226, 149))) {
                Text(if (isPaused) "Reanudar" else "Pausar")
            }

            Button(onClick = { showCancelDialog.value = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(255, 102, 102))) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
fun RutinasDelDiaCard(
    rutina: DataClassCaracteristicasEntrenamientos,
    estado: EstadoRutina,
    isPaused: Boolean,
    onRutinaTerminada: () -> Unit
) {
    var seriesRestantes by remember { mutableStateOf(rutina.series) }
    var descansoEnCurso by remember { mutableStateOf(false) }
    var tiempoRestante by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    val borderColor = when (estado) {
        EstadoRutina.ACTIVA -> Color(195, 255, 102)
        EstadoRutina.PENDIENTE -> Color(255, 226, 149)
        EstadoRutina.COMPLETADA -> Color(228, 228, 228)
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = rutina.categoria ?: "Entrenamiento sin nombre", fontSize = 20.sp, color = Color.Black)
            Text(text = rutina.subcategorias?.joinToString(", ") ?: "Subcategor\u00eda sin nombre", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "Repeticiones: ${rutina.repeticiones}", fontSize = 14.sp, color = Color.Black)
            Text(text = "Series restantes: $seriesRestantes", fontSize = 14.sp, color = Color.Black)

            if (descansoEnCurso) {
                Text(text = "Descanso: $tiempoRestante s", fontSize = 14.sp, color = Color(255, 202, 90))
            }

            Button(
                onClick = {
                    if (seriesRestantes > 0 && !descansoEnCurso && !isPaused) {
                        seriesRestantes--
                        descansoEnCurso = true
                        tiempoRestante = rutina.descanso
                        scope.launch {
                            while (tiempoRestante > 0) {
                                delay(1000)
                                if (!isPaused) tiempoRestante--
                            }
                            descansoEnCurso = false
                            if (seriesRestantes == 0) {
                                onRutinaTerminada()
                            }
                        }
                    }
                },
                enabled = estado == EstadoRutina.ACTIVA && seriesRestantes > 0 && !descansoEnCurso && !isPaused,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(195, 255, 102),
                    disabledContainerColor = Color(228, 228, 228),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray
                )
            ) {
                Text(">")
            }
        }
    }
}
