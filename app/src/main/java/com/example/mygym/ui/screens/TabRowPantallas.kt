package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun TabRowPantallas(
    navController: NavController,
    viewModel: MainViewModel,
    viewModelCaracteristicas: CaracteristicasEntrenamientoViewModel,
    dataUserViewModel: DataUserViewModel,
    calendarViewModel: CalendarViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Perfil", "Estadísticas", "Progreso")


    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.Black
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> PaginaPrincipal(
                navController,
                viewModel,
                viewModelCaracteristicas,
                dataUserViewModel,
                calendarViewModel
            )

            1 -> PaginaEntrenamiento(
                navController,
                viewModel,
                viewModelCaracteristicas,
                dataUserViewModel,
                calendarViewModel
            )

            2 -> PaginaCalendario(
                navController,
                viewModel,
                calendarViewModel,
                dataUserViewModel,
                viewModelCaracteristicas
            )
        }
    }
}
