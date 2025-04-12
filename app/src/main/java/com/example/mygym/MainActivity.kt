package com.example.mygym

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.screens.EdicionEntrenamiento
import com.example.mygym.ui.screens.PaginaCalendario
import com.example.mygym.ui.Entrenamiento.PaginaCategoriaEntrenamientos
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel
import com.example.mygym.ui.PerfilUsuario.PaginaDataUser
import com.example.mygym.ui.screens.PaginaEntrenamiento
import com.example.mygym.ui.screens.PaginaPrincipal
import com.example.mygym.ui.screens.TabRowPantallas
import com.example.mygym.ui.theme.MygymTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val categoriaEntrenamientoViewModel: CaracteristicasEntrenamientoViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MygymTheme {
                val navController = rememberNavController()
                val calendarViewModel: CalendarViewModel = viewModel()
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val dataUserViewModel: DataUserViewModel = viewModel()
                NavHost(
                    navController = navController,
                    startDestination = "tabRowPantallas",
                    builder = {
                        composable("paginaPrincipal") {
                            PaginaPrincipal(
                                navController = navController,
                                viewModel = mainViewModel,
                                viewModelCaracteristicas = CaracteristicasEntrenamientoViewModel(),
                                dataUserViewModel = dataUserViewModel,
                                calendarViewModel = CalendarViewModel()
                            )
                        }
                        composable("paginaCrearEntrenamiento") {
                            PaginaEntrenamiento(
                                navController = navController,
                                viewModel = mainViewModel,
                                calendarViewModel = CalendarViewModel(),
                                viewModelCaracteristicas = CaracteristicasEntrenamientoViewModel(),
                                dataUserViewModel = dataUserViewModel
                            )
                        }
                        composable("paginaCalendario") {
                            PaginaCalendario(
                                navController = navController,
                                viewModel = mainViewModel,
                                calendarViewModel = CalendarViewModel(),
                                dataUserViewModel = dataUserViewModel,
                                viewModelCaracteristicas = CaracteristicasEntrenamientoViewModel()
                            )
                        }
                        composable("edicionEntrenamiento") {
                            EdicionEntrenamiento(
                                navController = navController,
                                viewModel = mainViewModel
                            )
                        }
                        composable("categoriaEntrenamientos") {
                            PaginaCategoriaEntrenamientos(
                                navController = navController,
                                viewModelCategoria = CaracteristicasEntrenamientoViewModel(),
                                userId = userId
                            )
                        }

                        composable("paginaDataUser") {
                            PaginaDataUser(
                                navController = navController,
                                dataUserViewModel = dataUserViewModel
                            )
                        }

                        composable("tabRowPantallas"){
                            TabRowPantallas(
                                navController = navController,
                                dataUserViewModel = dataUserViewModel,
                                calendarViewModel = CalendarViewModel(),
                                viewModelCaracteristicas = CaracteristicasEntrenamientoViewModel(),
                                viewModel = mainViewModel
                            )
                        }
                    })
            }
        }
    }
}



