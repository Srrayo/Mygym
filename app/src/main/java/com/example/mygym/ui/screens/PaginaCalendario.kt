package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaginaCalendario(
    navController: NavController,
    viewModel: MainViewModel,
    calendarViewModel: CalendarViewModel,
    dataUserViewModel: DataUserViewModel,
    viewModelCaracteristicas: CaracteristicasEntrenamientoViewModel,
) {
    val fechasConEntrenamiento by calendarViewModel.fechasConEntrenamiento.collectAsState()
    val notasEntrenamiento by calendarViewModel.notasEntrenamiento.collectAsState()

    LaunchedEffect(Unit) {
        calendarViewModel.cargarEntrenamientos()
        calendarViewModel.cargarNotas()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CalendarApp(
            calendarViewModel = calendarViewModel,
            fechasConEntrenamiento = fechasConEntrenamiento,
            notasEntrenamiento = notasEntrenamiento
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarApp(
    calendarViewModel: CalendarViewModel,
    fechasConEntrenamiento: Set<LocalDate>,
    notasEntrenamiento: Map<LocalDate, String>
) {
    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(null) }
    var notaTexto by remember { mutableStateOf("") }
    var fechaActual by remember { mutableStateOf(LocalDate.now()) }

    val scrollState = rememberScrollState()
    val mesActual = fechaActual.month
    val anyoActual = fechaActual.year
    val diaEnMes = mesActual.length(fechaActual.isLeapYear)
    val primerDiaMes = LocalDate.of(anyoActual, mesActual, 1)
    val primerDiaSemana = (primerDiaMes.dayOfWeek.value - 1) % 7
    val nombreMes = mesActual.getDisplayName(TextStyle.FULL, Locale.getDefault())

    LaunchedEffect(fechaSeleccionada) {
        fechaSeleccionada?.let { fecha ->
            notaTexto = notasEntrenamiento[fecha] ?: ""
        }
    }

    val dias = buildList {
        val diaMesAnterior = primerDiaSemana
        if (diaMesAnterior > 0) {
            val prevMonth = mesActual.minus(1)
            val prevYear = if (mesActual == Month.JANUARY) anyoActual - 1 else anyoActual
            val daysInPrevMonth = Month.valueOf(prevMonth.name)
                .length(LocalDate.of(prevYear, prevMonth, 1).isLeapYear)

            for (i in diaMesAnterior downTo 1) {
                add(LocalDate.of(prevYear, prevMonth, daysInPrevMonth - i + 1))
            }
        }

        for (day in 1..diaEnMes) {
            add(LocalDate.of(anyoActual, mesActual, day))
        }

        val celdasRestantes = if ((diaEnMes + primerDiaSemana) % 7 == 0) 0
        else 7 - ((diaEnMes + primerDiaSemana) % 7)
        if (celdasRestantes > 0) {
            val mesSiguiente = mesActual.plus(1)
            val anyoSiguiente = if (mesActual == Month.DECEMBER) anyoActual + 1 else anyoActual

            for (day in 1..celdasRestantes) {
                add(LocalDate.of(anyoSiguiente, mesSiguiente, day))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(236, 240, 241))
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    fechaActual = fechaActual.minusMonths(1)
                    calendarViewModel.cargarEntrenamientos()
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes Anterior", tint = Color.Black)
            }

            Text(
                text = "$nombreMes $anyoActual",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Black,
            )

            IconButton(
                onClick = {
                    fechaActual = fechaActual.plusMonths(1)
                    calendarViewModel.cargarEntrenamientos()
                }
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes Siguiente", tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.heightIn(max = 500.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(dias) { date ->
                val diaDelMes = date.dayOfMonth
                val esMesActual = date.month == mesActual
                val esHoy = date == LocalDate.now()
                val estaSeleccionado = fechaSeleccionada == date
                val tieneEntrenamiento = fechasConEntrenamiento.contains(date)
                val tieneNota = notasEntrenamiento.containsKey(date)

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                esHoy && estaSeleccionado -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                esHoy -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                estaSeleccionado -> MaterialTheme.colorScheme.primary
                                tieneEntrenamiento -> if (tieneNota) Color(0xFF4CAF50).copy(alpha = 0.7f)
                                else Color(0xFFFFA000).copy(alpha = 0.7f)
                                else -> Color.Transparent
                            }
                        )
                        .border(
                            width = if (tieneEntrenamiento) 2.dp else 1.dp,
                            color = when {
                                tieneNota -> Color(0xFF388E3C)
                                tieneEntrenamiento -> Color.Yellow
                                esMesActual -> MaterialTheme.colorScheme.outline
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { fechaSeleccionada = date },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = diaDelMes.toString(),
                            color = when {
                                !esMesActual -> Color.Gray
                                estaSeleccionado -> MaterialTheme.colorScheme.onPrimary
                                else -> Color.Black
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (tieneNota) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFF388E3C))
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        fechaSeleccionada?.let { date ->
            val notaGuardada = notasEntrenamiento[date]

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Notas para ${date.dayOfMonth} de ${
                        date.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        )
                    }",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                notaGuardada?.let { nota ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = nota,
                            modifier = Modifier.padding(12.dp),
                            color = Color.Black
                        )
                    }
                }

                OutlinedTextField(
                    value = notaTexto,
                    onValueChange = { notaTexto = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = {
                        Text(
                            if (notaGuardada == null) "Escribe tus notas aquí..."
                            else "Editar nota existente..."
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            fechaSeleccionada?.let {
                                calendarViewModel.guardarEntrenamiento(it, notaTexto)
                                notaTexto = ""
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (notaGuardada == null) "Guardar Nota" else "Actualizar Nota")
                    }

                    if (notaGuardada != null) {
                        Button(
                            onClick = {
                                fechaSeleccionada?.let {
                                    calendarViewModel.eliminarNota(it)
                                    notaTexto = ""
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Eliminar Nota")
                        }
                    }
                }
            }
        }
    }
}