package com.example.mygym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygym.model.Entrenamientos
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.screens.PaginaPrincipal
import com.example.mygym.ui.screens.PaginaPrueba
import com.example.mygym.ui.theme.MygymTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MygymTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "paginaPrincipal", builder = {
                    composable("paginaPrincipal"){
                        PaginaPrincipal(navController, mainViewModel)
                    }
                    composable("paginaPrueba"){
                        PaginaPrueba(navController)
                    }
                })
                }
            }
        }
    }



