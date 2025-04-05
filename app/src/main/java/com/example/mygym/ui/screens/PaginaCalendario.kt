package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun PaginaCalendario(
    navController: NavController,
    viewModel: MainViewModel,
    calendarViewModel: CalendarViewModel,
    dataUserViewModel: DataUserViewModel,
    viewModelCaracteristicas: CaracteristicasEntrenamientoViewModel
) {
    MenuLateral(navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            HeaderPaginaPrincipal(navController,viewModel,viewModelCaracteristicas,dataUserViewModel,calendarViewModel)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Btn_PaginaPrincipal(navController)
//                Btn_PaginaCrearEntrenamiento(navController)
//                Btn_Calendario(navController)
            }
            Spacer(modifier = Modifier.height(16.dp))
            CalendarioApp()


        }
    }
}
