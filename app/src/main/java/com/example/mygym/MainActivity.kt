package com.example.mygym

import CaracteristicasEntrenamientoViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygym.model.CaracteristicasEntrenamientos
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.screens.CrearEntrenamiento
import com.example.mygym.ui.screens.EdicionEntrenamiento
import com.example.mygym.ui.screens.InicioSesion
import com.example.mygym.ui.screens.PaginaCalendario
import com.example.mygym.ui.Entrenamiento.PaginaCategoriaEntrenamientos
import com.example.mygym.ui.screens.PaginaPrincipal
import com.example.mygym.ui.screens.PaginaPrueba
import com.example.mygym.ui.theme.MygymTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val categoriaEntrenamientoViewModel: CaracteristicasEntrenamientoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MygymTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "paginaPrincipal",
                    builder = {
                        composable("paginaPrincipal") {
                            PaginaPrincipal(navController = navController, viewModel = mainViewModel)
                        }
                        composable("paginaCrearEntrenamiento") {
                            PaginaPrueba(navController = navController, viewModel = mainViewModel)
                        }
                        composable("paginaCalendario") {
                            PaginaCalendario(navController = navController, viewModel = mainViewModel)
                        }
                        composable("crearEntrenamiento") {
                            CrearEntrenamiento(navController = navController, viewModel = mainViewModel)
                        }
                        composable("inicioSesion") {
                            InicioSesion(navController = navController, viewModel = mainViewModel)
                        }
                        composable("edicionEntrenamiento"){
                            EdicionEntrenamiento(navController = navController, viewModel = mainViewModel)
                        }
                        composable("categoriaEntrenamientos"){
                            PaginaCategoriaEntrenamientos(
                                navController = navController,
                                viewModelCategoria = categoriaEntrenamientoViewModel)
                        }
                    })
            }
        }
    }
}



