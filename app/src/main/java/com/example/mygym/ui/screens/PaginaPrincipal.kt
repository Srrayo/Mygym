package com.example.mygym.ui.screens

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.R
import com.example.mygym.model.Entrenamientos
import com.example.mygym.model.MainViewModel
import com.example.mygym.ui.theme.MygymTheme


@Composable
fun PaginaPrincipal(navController: NavController, viewModel: MainViewModel) {
    val entrenamientos = viewModel.entrenamientos
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Icon(Icons.Rounded.Menu, contentDescription = "menuBusqueda",tint = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.logob),
                contentDescription = "logo",
                modifier = Modifier
                    .size(100.dp)
                    .weight(3f)
            )
            Spacer(modifier = Modifier.weight(3f))
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Hola, " + viewModel.nombreUsuario, color = Color.White, fontSize = 30.sp)
            Text(text = "25", color = Color.White, fontSize = 30.sp)
        }

        Spacer(modifier = Modifier.height(80.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp), Arrangement.SpaceEvenly, Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {navController.navigate("paginaPrincipal")},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = "Entrenamiento",
                            color = Color.Black,
                            fontSize = 12.sp,
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline,
                                fontSize = 12.sp
                            )
                        )
                    }
                    Button(
                        onClick = {navController.navigate("paginaCrearEntrenamiento")},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = "Crear",
                            color = Color.Black,
                            fontSize = 12.sp,
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline,
                                fontSize = 12.sp
                            )
                        )
                    }
                    Button(
                        onClick = {navController.navigate("paginaCalendario")},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = "Calendario",
                            color = Color.Black,
                            fontSize = 12.sp,
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
                LazyEntrenamiento(entrenamientos = entrenamientos)
            }
        }
    }
}

@Composable

fun LazyEntrenamiento(entrenamientos: List<Entrenamientos>) {
    if (entrenamientos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay entrenamientos disponibles",
                style = TextStyle(fontSize = 20.sp, color = Color.Gray)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(entrenamientos) { entrenamiento ->
                CardEntrenamientos(entrenamiento = entrenamiento)
            }
        }
    }
}

@Composable
fun CardEntrenamientos(entrenamiento: Entrenamientos) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = entrenamiento.dia,
                    style = TextStyle(fontSize = 20.sp, color = Color.White)
                )
                Text(
                    text = "->",
                    color = Color.White
                )
                Text(
                    text = entrenamiento.nombre,
                    style = TextStyle(fontSize = 20.sp, color = Color.White)
                )

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor  = Color.Gray,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Iniciar", color = Color.White)
                }
            }

        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = entrenamiento.entrenamiento,
                style = TextStyle(fontSize = 15.sp, color = Color.White)
            )
        }
    }
}
