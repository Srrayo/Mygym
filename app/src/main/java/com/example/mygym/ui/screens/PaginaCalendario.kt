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
import androidx.compose.foundation.shape.RoundedCornerShape
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
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CalendarApp(calendarViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarApp(
    calendarViewModel: CalendarViewModel
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var noteText by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    val currentMonth = currentDate.month
    val currentYear = currentDate.year
    val daysInMonth = currentMonth.length(currentDate.isLeapYear)
    val firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1)
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value - 1) % 7
    val monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())

    val days = buildList {
        // Días del mes anterior
        val prevMonthDays = firstDayOfWeek
        if (prevMonthDays > 0) {
            val prevMonth = currentMonth.minus(1)
            val prevYear = if (currentMonth == Month.JANUARY) currentYear - 1 else currentYear
            val daysInPrevMonth = Month.valueOf(prevMonth.name)
                .length(LocalDate.of(prevYear, prevMonth, 1).isLeapYear)

            for (i in prevMonthDays downTo 1) {
                add(LocalDate.of(prevYear, prevMonth, daysInPrevMonth - i + 1))
            }
        }

        // Días del mes actual
        for (day in 1..daysInMonth) {
            add(LocalDate.of(currentYear, currentMonth, day))
        }

        // Días del siguiente mes
        val totalCells = if ((daysInMonth + firstDayOfWeek) % 7 == 0) 0
        else 7 - ((daysInMonth + firstDayOfWeek) % 7)
        if (totalCells > 0) {
            val nextMonth = currentMonth.plus(1)
            val nextYear = if (currentMonth == Month.DECEMBER) currentYear + 1 else currentYear

            for (day in 1..totalCells) {
                add(LocalDate.of(nextYear, nextMonth, day))
            }
        }
    }

    // Estado de fechas con entrenamientos
    val fechasConEntrenamiento = calendarViewModel.fechasConEntrenamiento.collectAsState().value

    // Cargar entrenamientos cuando se inicie la pantalla
    LaunchedEffect(Unit) {
        calendarViewModel.cargarEntrenamientos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(236, 240, 241))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Navegación entre meses
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentDate = currentDate.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes Anterior", tint = Color.Black)
            }

            Text(
                text = "$monthName $currentYear",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Black,
            )

            IconButton(onClick = { currentDate = currentDate.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes Siguiente", tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Días de la semana
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

        // Grid de días
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days) { date ->
                val dayOfMonth = date.dayOfMonth
                val isCurrentMonth = date.month == currentMonth
                val isToday = date == LocalDate.now()
                val isSelected = selectedDate == date
                val isEntrenamiento = fechasConEntrenamiento.contains(date)

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                isToday && isSelected -> MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.7f
                                )

                                isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                isSelected -> MaterialTheme.colorScheme.primary
                                isEntrenamiento -> Color.Yellow.copy(alpha = 0.4f)
                                else -> Color.Transparent
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isCurrentMonth) MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedDate = date },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayOfMonth.toString(),
                        color = when {
                            !isCurrentMonth -> Color.Gray
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            else -> Color.Black
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notas para la fecha seleccionada
        selectedDate?.let { date ->
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
                    style = MaterialTheme.typography.bodyLarge
                )

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = { Text("Escribe tus notas aquí...") },
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        selectedDate?.let {
                            calendarViewModel.guardarEntrenamiento(it, noteText)
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Guardar Notas")
                }
            }
        }
    }
}
