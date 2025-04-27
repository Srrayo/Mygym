package com.example.mygym

import CaracteristicasEntrenamientoViewModel
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.EditarEntrenamiento.EditarEntrenamientoViewModel
import com.example.mygym.ui.EditarEntrenamiento.PantallaEdicionEjercicio
//import com.example.mygym.ui.EditarEntrenamiento.PantallaEdicionEjercicio
//import com.example.mygym.ui.EditarEntrenamiento.PantallaEdicionEntrenamiento
import com.example.mygym.ui.screens.EdicionEntrenamiento
import com.example.mygym.ui.screens.PaginaCalendario
import com.example.mygym.ui.Entrenamiento.PaginaCategoriaEntrenamientos
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel
import com.example.mygym.ui.PerfilUsuario.PaginaDataUser
import com.example.mygym.ui.screens.PaginaEntrenamiento
import com.example.mygym.ui.screens.PaginaPrincipal
import com.example.mygym.ui.screens.PantallaEditarRutina
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
                val editarEntrenamientoViewModel: EditarEntrenamientoViewModel = viewModel()
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
//                                viewModel = mainViewModel,
//                                calendarViewModel = CalendarViewModel(),
                                viewModel = CaracteristicasEntrenamientoViewModel(),
//                                dataUserViewModel = dataUserViewModel
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

                        composable("tabRowPantallas") {
                            TabRowPantallas(
                                navController = navController,
                                dataUserViewModel = dataUserViewModel,
                                calendarViewModel = CalendarViewModel(),
                                viewModelCaracteristicas = CaracteristicasEntrenamientoViewModel(),
                                viewModel = mainViewModel
                            )
                        }

                        composable(
                            "editar_ejercicio/{bloqueId}/{rutinaKey}/{nombreEntrenamiento}/{categoria}/{subcategoria}/{dias}/{descanso}/{repeticiones}/{series}",
                            arguments = listOf(
                                navArgument("bloqueId") { type = NavType.StringType },
                                navArgument("rutinaKey") { type = NavType.StringType },
                                navArgument("nombreEntrenamiento") { type = NavType.StringType },
                                navArgument("categoria") { type = NavType.StringType },
                                navArgument("subcategoria") { type = NavType.StringType },
                                navArgument("dias") { type = NavType.StringType },
                                navArgument("descanso") { type = NavType.IntType },
                                navArgument("repeticiones") { type = NavType.IntType },
                                navArgument("series") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val bloqueId = backStackEntry.arguments?.getString("bloqueId") ?: ""
                            val rutinaKey = backStackEntry.arguments?.getString("rutinaKey") ?: ""
                            val nombre = backStackEntry.arguments?.getString("nombreEntrenamiento") ?: ""
                            val categoria = backStackEntry.arguments?.getString("categoria") ?: ""
                            val subcategoria = backStackEntry.arguments?.getString("subcategoria") ?: ""
                            val dias = backStackEntry.arguments?.getString("dias")?.split(",") ?: emptyList()
                            val descanso = backStackEntry.arguments?.getInt("descanso") ?: 0
                            val repeticiones = backStackEntry.arguments?.getInt("repeticiones") ?: 0
                            val series = backStackEntry.arguments?.getInt("series") ?: 0

                            val ejercicio = DataClassCaracteristicasEntrenamientos(
                                subcategorias = listOf(subcategoria),
                                nombreEntrenamiento = nombre,
                                dias = dias,
                                categoria = categoria,
                                descanso = descanso,
                                repeticiones = repeticiones,
                                series = series,
                                bloqueId = bloqueId,
                                nombre = null
                            )

                            PantallaEdicionEjercicio(
                                ejercicio = ejercicio,
                                viewModel = CaracteristicasEntrenamientoViewModel(),
                                navController = navController,
                                rutinaKey = rutinaKey
                            )
                        }

                        composable(
                            route = "editar_rutina/{bloqueId}/{nombreEntrenamiento}/{categoria}/{subcategorias}/{dias}/{descanso}/{series}/{repeticiones}",
                            arguments = listOf(
                                navArgument("bloqueId") { type = NavType.StringType },
                                navArgument("nombreEntrenamiento") { type = NavType.StringType },
                                navArgument("categoria") { type = NavType.StringType },
                                navArgument("subcategorias") { type = NavType.StringType },
                                navArgument("dias") { type = NavType.StringType },
                                navArgument("descanso") { type = NavType.IntType },
                                navArgument("series") { type = NavType.IntType },
                                navArgument("repeticiones") { type = NavType.IntType },
                            )
                        ) { backStackEntry ->

                            val bloqueId = backStackEntry.arguments?.getString("bloqueId") ?: ""
                            val nombreEntrenamiento = backStackEntry.arguments?.getString("nombreEntrenamiento") ?: ""
                            val categoria = backStackEntry.arguments?.getString("categoria") ?: ""
                            val subcategorias = backStackEntry.arguments?.getString("subcategorias")?.split(",") ?: emptyList()
                            val dias = backStackEntry.arguments?.getString("dias")?.split(",") ?: emptyList()
                            val descanso = backStackEntry.arguments?.getInt("descanso") ?: 0
                            val series = backStackEntry.arguments?.getInt("series") ?: 0
                            val repeticiones = backStackEntry.arguments?.getInt("repeticiones") ?: 0

                            Log.d("EditarRutina", "nombreEntrenamiento: $nombreEntrenamiento, categoria: $categoria")

                            PantallaEditarRutina(
                                navController = navController,
                                bloqueId = bloqueId,
                                nombreEntrenamiento = nombreEntrenamiento,
                                categoria = categoria,
                                subcategorias = subcategorias,
                                dias = dias,
                                descanso = descanso,
                                series = series,
                                repeticiones = repeticiones,
                                viewModel = CaracteristicasEntrenamientoViewModel()
                            )
                        }

                    })
            }
        }
    }
}