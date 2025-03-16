package com.example.mygym.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.LoginActivity
import com.example.mygym.R
import com.example.mygym.model.CaracteristicasEntrenamientos
import com.example.mygym.model.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@Composable
fun PaginaPrincipal(navController: NavController, viewModel: MainViewModel) {
    val entrenamientos = viewModel.entrenamientos

    MenuLateral(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding) // Asegurar que el contenido respete el padding del Scaffold
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(R.drawable.logob),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(100.dp),

                )
            }

            Spacer(modifier = Modifier.weight(3f))


            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hola, " + viewModel.nombreUsuario,
                    color = Color.White,
                    fontSize = 30.sp
                )
                Text(text = "25", color = Color.White, fontSize = 30.sp)
            }

            Spacer(modifier = Modifier.height(80.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        Arrangement.SpaceEvenly,
                        Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { navController.navigate("paginaPrincipal") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(
                                text = "Entrenamiento",
                                color = Color.Black,
                                fontSize = 12.sp,
                                style = TextStyle(textDecoration = TextDecoration.Underline)
                            )
                        }
                        Button(
                            onClick = { navController.navigate("paginaCrearEntrenamiento") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(
                                text = "Crear",
                                color = Color.Black,
                                fontSize = 12.sp,
                                style = TextStyle(textDecoration = TextDecoration.Underline)
                            )
                        }
                        Button(
                            onClick = { navController.navigate("paginaCalendario") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(
                                text = "Calendario",
                                color = Color.Black,
                                fontSize = 12.sp,
                                style = TextStyle(textDecoration = TextDecoration.Underline)
                            )
                        }


                    }
                    LazyEntrenamiento(entrenamientos = entrenamientos)
                }
                Button(onClick = { navController.navigate("categoriaEntrenamientos") },){
                    Text(
                        text = "categoria",
                        color = Color.Black,
                        fontSize = 12.sp,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
            }
        }
    }
}


@Composable

fun LazyEntrenamiento(entrenamientos: List<CaracteristicasEntrenamientos>) {
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
fun CardEntrenamientos(entrenamiento: CaracteristicasEntrenamientos) {
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
            ) {
                /**
                 * Text(
                 *                    // text = entrenamiento.dia,
                 *                    // style = TextStyle(fontSize = 20.sp, color = Color.White)
                 *                 )
                 *                 Text(text = " | ", color = Color.White)
                 *
                 *                 entrenamiento.categoriaEntrenamientos.forEach { categoria ->
                 *                     Text(
                 *                         text = categoria.name,
                 *                         style = TextStyle(fontSize = 12.sp, color = Color.White),
                 *                     )
                 *
                 *                 }
                 */

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Iniciar", color = Color.White)
                }
            }

            /**
             * Text(
             *                 text = entrenamiento.nombre,
             *                 style = TextStyle(fontSize = 20.sp, color = Color.White)
             *             )
             */


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

            }

        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

        }
    }
}


@Composable
fun LogoutButton(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current // Obtén el contexto actual

    Button(
        onClick = {
            auth.signOut() // Cierra la sesión del usuario

            // Crea un Intent para iniciar LoginActivity
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK // Limpia la pila de actividades
            }
            context.startActivity(intent) // Inicia LoginActivity

            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Logout",
            color = Color.White,
            style = TextStyle(fontSize = 10.sp, textDecoration = TextDecoration.None)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuLateral(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Menú",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()

                    Text(
                        "Section 1",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 1") },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 2") },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        "Sesión",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    NavigationDrawerItem(
                        label = { Text("Ajustes") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        onClick = { /* Handle click */ }
                    )

                    Spacer(Modifier.height(40.dp))
                    LogoutButton(navController = navController)
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
