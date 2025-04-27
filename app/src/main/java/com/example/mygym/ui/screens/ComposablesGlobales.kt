package com.example.mygym.ui.screens

import CaracteristicasEntrenamientoViewModel
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.LoginActivity
import com.example.mygym.R
import com.example.mygym.model.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygym.model.CalendarViewModel
import com.example.mygym.ui.PerfilUsuario.DataUserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// -- ↓ Logo e info del usuario ↓ ---------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderPaginaPrincipal(
    navController: NavController,
    viewModel: MainViewModel,
    viewModelCaracteristicas: CaracteristicasEntrenamientoViewModel,
    dataUserViewModel: DataUserViewModel,
    calendarViewModel: CalendarViewModel,
) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: return
    var nombre by remember { mutableStateOf("") }
    val usuario = dataUserViewModel.usuario
    LaunchedEffect(userId) {
        dataUserViewModel.loadUserData(userId)
    }
    LaunchedEffect(usuario) {
        nombre = usuario.nombre
    }

    Column(
        Modifier
            .background(Color(44, 44, 44))
            .padding(10.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(44, 44, 44)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logob),
                contentDescription = "logo",
                modifier = Modifier
                    .size(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hola, $nombre",
                color = Color.White,
                fontSize = 30.sp
            )
            Text(text = "25", color = Color.White, fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
//        TabRowPantallas(navController,viewModel,viewModelCaracteristicas,dataUserViewModel,calendarViewModel)
    }
}
// -- ↑ Logo e info del usuario ↑ ---------------------------------------------------------------------------

@Composable
fun HeaderPaginaEntrenamiento(
    navController: NavController,
    viewModel: MainViewModel,
    viewModelCaracteristicas: CaracteristicasEntrenamientoViewModel,
    dataUserViewModel: DataUserViewModel,
    calendarViewModel: CalendarViewModel
) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: return
    var nombre by remember { mutableStateOf("") }
    val usuario = dataUserViewModel.usuario
    LaunchedEffect(userId) {
        dataUserViewModel.loadUserData(userId)
    }
    LaunchedEffect(usuario) {
        nombre = usuario.nombre
    }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logongro),
                contentDescription = "logo",
                modifier = Modifier
                    .size(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hola, $nombre",
                color = Color.Black,
                fontSize = 30.sp
            )
            Text(text = "25", color = Color.Black, fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

// -- ↓ Boton pagina principal ↓ ----------------------------------------------------------------------------
//@Composable
//fun Btn_PaginaPrincipal(navController: NavController) {
//    val currentRoute = navController.currentBackStackEntry?.destination?.route
//    Button(
//        onClick = { navController.navigate("paginaPrincipal") },
//        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//    ) {
//        Text(
//            text = "Entrenamiento",
//            color = Color.Black,
//            fontSize = 12.sp,
//            style = if (currentRoute == "paginaPrincipal") {
//                TextStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)
//            } else {
//                TextStyle()
//            }
//        )
//    }
//}
// -- ↑  Boton pagina principal ↑ ---------------------------------------------------------------------------

// -- ↓ Boton pagina paginaCrearEntrenamiento ↓ -------------------------------------------------------------
//@Composable
//fun Btn_PaginaCrearEntrenamiento(navController: NavController) {
//    val currentRoute = navController.currentBackStackEntry?.destination?.route
//    Button(
//        onClick = { navController.navigate("paginaCrearEntrenamiento") },
//        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//    ) {
//        Text(
//            text = "Crear",
//            color = Color.Black,
//            fontSize = 12.sp,
//            style = if (currentRoute == "paginaCrearEntrenamiento") {
//                TextStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)
//            } else {
//                TextStyle()
//            }
//        )
//    }
//}
// -- ↑  Boton pagina paginaCrearEntrenamiento ↑ -------------------------------------------------------------

// -- ↓ Boton pagina Calendario ↓ ----------------------------------------------------------------------------
//@Composable
//fun Btn_Calendario(navController: NavController) {
//    val currentRoute = navController.currentBackStackEntry?.destination?.route
//    Button(
//        onClick = { navController.navigate("paginaCalendario") },
//        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//    ) {
//        Text(
//            text = "Calendario",
//            color = Color.Black,
//            fontSize = 12.sp,
//            style = if (currentRoute == "paginaCalendario") {
//                TextStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)
//            } else {
//                TextStyle()
//            }
//        )
//    }
//}
// -- ↑  Boton pagina Calendario ↑ ---------------------------------------------------------------------------

// -- ↓ Boton pagina Categoria entrenamiendos ↓ --------------------------------------------------------------
//@Composable
//fun Btn_CategoriaEntrenamientos(navController: NavController) {
//    val currentRoute = navController.currentBackStackEntry?.destination?.route
//    Button(
//        onClick = { navController.navigate("categoriaEntrenamientos") },
//        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//    ) {
//        Text(
//            text = "CategoriaEntrenamiento",
//            color = Color.Black,
//            fontSize = 12.sp,
//            style = if (currentRoute == "categoriaEntrenamientos") {
//                TextStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)
//            } else {
//                TextStyle()
//            }
//        )
//    }
//}
// -- ↑  Boton pagina Categoria entrenamiendos ↑ ------------------------------------------------------------

// -- ↓ Logout button ↓ -------------------------------------------------------------------------------------
@Composable
fun LogoutButton(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    Button(
        onClick = {
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
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
// -- ↑ Logout button ↑ -------------------------------------------------------------------------------------

// -- ↓ Menu lateral ↓ --------------------------------------------------------------------------------------
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
            ModalDrawerSheet(modifier = Modifier.background(Color.Transparent)) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(Color.Transparent)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Menú",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()

                    NavigationDrawerItem(
                        label = { Text("Perfil") },
                        selected = false,
                        onClick = { navController.navigate("paginaDataUser") },
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 2") },
                        selected = false,
                        onClick = { }
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
                        onClick = { }
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
// -- ↑ Menu lateral ↑ --------------------------------------------------------------------------------

//@Composable
//fun CalendarioApp(viewModel: CalendarViewModel = viewModel()) {
//    var showDatePickerDialog by remember { mutableStateOf(false) }
//    var selectedDate by remember { mutableStateOf("Selecciona una fecha") }
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Primer Calendario",
//            style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Text(
//            text = selectedDate,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(8.dp)
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(onClick = { showDatePickerDialog = true }) {
//            Text("Seleccionar fecha")
//        }
//
//        if (showDatePickerDialog) {
//            DatePickerDialog(
//                onDismissRequest = { showDatePickerDialog = false },
//                onDateSelected = { date ->
//                    selectedDate = date
//                    val dateMillis = parseDate(date)
//                    dateMillis?.let { viewModel.addSelectedDate(it) }
//                    showDatePickerDialog = false
//                }
//            )
//        }
//
//        Spacer(modifier = Modifier.height(40.dp))
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerDialog(
//    onDismissRequest: () -> Unit,
//    onDateSelected: (String) -> Unit
//) {
//    val datePickerState = rememberDatePickerState()
//
//    Dialog(
//        onDismissRequest = onDismissRequest,
//        properties = DialogProperties(
//            dismissOnBackPress = true,
//            dismissOnClickOutside = true
//        )
//    ) {
//        Surface(
//            modifier = Modifier.fillMaxWidth(),
//            shape = MaterialTheme.shapes.medium
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//                DatePicker(
//                    state = datePickerState,
//                    title = {
//                        Text("Selecciona una fecha")
//                    },
//                    headline = {
//                        Text(
//                            "Fecha seleccionada: ${
//                                datePickerState.selectedDateMillis?.let {
//                                    formatDate(
//                                        it
//                                    )
//                                } ?: "Ninguna"
//                            }")
//                    },
//                    showModeToggle = true
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        val selectedDateMillis = datePickerState.selectedDateMillis
//                        selectedDateMillis?.let {
//                            onDateSelected(formatDate(it))
//                        }
//                        onDismissRequest()
//                    }
//                ) {
//                    Text("Confirmar")
//                }
//            }
//        }
//    }
//}
//
//fun formatDate(millis: Long): String {
//    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//    return dateFormat.format(Date(millis))
//}
//
//
//@Composable
//fun SecondCalendar(viewModel: CalendarViewModel) {
//    val selectedDates = viewModel.selectedDates
//    val calendar = Calendar.getInstance()
//    val year = calendar.get(Calendar.YEAR)
//    val month = calendar.get(Calendar.MONTH)
//
//    val daysInMonth = getDaysInMonth(year, month)
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Segundo Calendario",
//            style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(7),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            items(daysInMonth) { day ->
//                val dateMillis = getDateMillis(year, month, day)
//                val isSelected = selectedDates.any { it.timestamp == dateMillis }
//
//                DayCell(day = day, isSelected = isSelected)
//            }
//        }
//    }
//}
//
//@Composable
//fun DayCell(day: Int, isSelected: Boolean) {
//    Box(
//        modifier = Modifier
//            .padding(4.dp)
//            .size(40.dp)
//            .background(
//                color = if (isSelected) Color.Green.copy(alpha = 0.3f) else Color.Transparent,
//                shape = CircleShape
//            )
//            .border(
//                width = 1.dp,
//                color = if (isSelected) Color.Green else Color.LightGray,
//                shape = CircleShape
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = if (day > 0) day.toString() else "",
//            style = MaterialTheme.typography.bodyMedium,
//            color = if (isSelected) Color.Green else Color.Black
//        )
//    }
//}
//
//private fun getDaysInMonth(year: Int, month: Int): List<Int> {
//    val calendar = Calendar.getInstance()
//    calendar.set(year, month, 1)
//    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
//
//    val daysInMonth = mutableListOf<Int>()
//    // Agregar espacios vacíos para los días que no pertenecen al mes
//    for (i in 1 until firstDayOfWeek) {
//        daysInMonth.add(0) // 0 representa un día vacío
//    }
//    // Agregar los días del mes
//    for (day in 1..maxDay) {
//        daysInMonth.add(day)
//    }
//    return daysInMonth
//}

@Composable
fun CerrarVentana(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { navController.navigate("tabRowPantallas") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Red
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "volver",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

//// Convertir un día, mes y año a milisegundos
//private fun getDateMillis(year: Int, month: Int, day: Int): Long {
//    val calendar = Calendar.getInstance()
//    calendar.set(year, month, day)
//    return calendar.timeInMillis
//}
//
//private fun parseDate(dateString: String): Long? {
//    return try {
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val date = dateFormat.parse(dateString)
//        date?.time
//    } catch (e: Exception) {
//        null
//    }
//}
