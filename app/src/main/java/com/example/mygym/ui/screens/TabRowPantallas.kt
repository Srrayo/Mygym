package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    calendarViewModel: CalendarViewModel,

) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Inicio", "Entrenamiento", "Calendario")

    MenuLateral(navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(35, 35, 35))
                .padding(innerPadding)
        ) {
            HeaderPaginaPrincipal(
                navController,
                viewModel,
                viewModelCaracteristicas,
                dataUserViewModel,
                calendarViewModel
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(44, 44, 44))
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
                    indicator = { tabPositions ->
                        val currentTabPosition = tabPositions[selectedTabIndex]
                        SecondaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(currentTabPosition),
                            color = Color(25, 25, 25 )
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        val backgroundColor = if (isSelected) {
                            Color(25, 25, 25 )
                        } else {
                            Color(70, 70, 70 )
                        }

                        Tab(
                            selected = isSelected,
                            onClick = { selectedTabIndex = index },
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(backgroundColor)
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(title, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
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
//                    viewModel,
                    viewModelCaracteristicas,
//                    dataUserViewModel,
//                    calendarViewModel
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
}
