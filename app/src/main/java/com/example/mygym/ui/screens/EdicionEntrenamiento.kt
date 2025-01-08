package com.example.mygym.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mygym.model.MainViewModel

@Composable

fun EdicionEntrenamiento(navController: NavController, viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(navController)
        },
        bottomBar = {

        },
    ) { innerPadding ->
        Content(innerPadding = innerPadding)
    }

}

@Composable

fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ){
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = { navController.navigate("paginaCrearEntrenamiento") })
        {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "playArrow",
                modifier = Modifier.size(20.dp),
                tint = Color.Red
            )
        }
    }

}

@Composable

fun Content(innerPadding: PaddingValues) {

}