package com.example.mygym.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable

fun PaginaPrueba(navController: NavController){
    Column(){
        Text("paginabB")
        Button(onClick = {navController.navigate("paginaPrincipal")}){
            Text(text="volver")
        }
    }
}