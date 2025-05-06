package com.example.mygym.ui.Entrenamiento

//-- ↓ Imports ↓ -------------------------------------------------

import CaracteristicasEntrenamientoViewModel
import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mygym.model.DataClassCaracteristicasEntrenamientos
import com.example.mygym.ui.screens.CerrarVentana
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//-- ↑ Imports ↑ -------------------------------------------------

@Composable
fun PaginaCategoriaEntrenamientos(
    viewModelCategoria: CaracteristicasEntrenamientoViewModel,
    navController: NavController,
    userId: String
) {
    val caracteristicasEntrenamientos by viewModelCategoria.entrenamiento.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var mostrarLista by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var subcategoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var nombreEntrenamiento by remember { mutableStateOf("") }
    var diasSeleccionados by remember { mutableStateOf(setOf<String>()) }
    val listaEjercicios = remember { mutableStateListOf<Map<String, Any>>() }
    val bloqueTimestamp = remember { System.currentTimeMillis() }
    val series = remember {mutableStateOf(0)}
    val repeticiones = remember {mutableStateOf(0)}
    val descanso = remember {mutableStateOf(0)}
    var showSeriesInput by remember { mutableStateOf(false) }
    var showRepeticionesInput by remember { mutableStateOf(false) }
    var showDescansoInput by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModelCategoria.getEntrenamiento(userId)
        }
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(Color(44, 44, 44))
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if ( categoriaSeleccionada != null &&
                            subcategoriaSeleccionada != null &&
                            diasSeleccionados.isNotEmpty() &&
                            series.value > 0 &&
                            repeticiones.value > 0 &&
                            descanso.value > 0) {
                            val nuevoEjercicio = mapOf(
                                "nombreEntrenamiento" to nombreEntrenamiento,
                                "categoria" to categoriaSeleccionada!!,
                                "subcategoria" to subcategoriaSeleccionada!!,
                                "dias" to diasSeleccionados.toList(),
                                "series" to series.value,
                                "repeticiones" to repeticiones.value,
                                "descanso" to descanso.value
                            )
                            listaEjercicios.add(nuevoEjercicio)

                            categoriaSeleccionada = null
                            subcategoriaSeleccionada = null

                            Toast.makeText(context, "Ejercicio añadido ✔\uFE0F", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Rellene todos los campos ⚠\uFE0F",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(244, 208, 63),
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.padding(bottom = 15.dp, top = 15.dp, start = 15.dp)
                ) {
                    Text("Añadir nuevo")
                }

                Spacer(modifier = Modifier.width(20.dp))

                Button(
                    onClick = {
                        if (listaEjercicios.isNotEmpty()) {
                            listaEjercicios.forEachIndexed { index, rutina ->
                                val categoria = rutina["categoria"] as String
                                val subcategoria = rutina["subcategoria"] as String

                                viewModelCategoria.guardarRutinaEnFirestore(
                                    userId,
                                    categoria,
                                    subcategoria,
                                    nombreEntrenamiento,
                                    diasSeleccionados,
                                    descanso.value,
                                    repeticiones.value,
                                    series.value,
                                    bloqueTimestamp,
                                    index
                                )
                            }

                            Toast.makeText(
                                context,
                                "Rutinas guardadas en Firebase",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigate("tabRowPantallas")
                        } else {
                            Toast.makeText(
                                context,
                                "No hay rutinas para guardar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 15.dp, top = 15.dp, end = 15.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Guardar")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(236, 240, 241))
                .padding(paddingValues),
        ) {
            item {
                CerrarVentana(navController)
            }

            item {
                TextField(
                    value = nombreEntrenamiento,
                    onValueChange = {
                        nombreEntrenamiento = it
                        listaEjercicios.replaceAll { ejercicio ->
                            ejercicio.toMutableMap().apply { this["nombreEntrenamiento"] = it }
                        }
                    },
                    label = { Text(text = "Nombre del entrenamiento") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(5.dp))

                Button(
                    onClick = {
                        mostrarLista = !mostrarLista
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(95, 95, 95),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp)
                ) {
                    Text(text = if (mostrarLista) "Ocultar Categorías" else "Mostrar Categorías")
                }
            }

            if (mostrarLista) {
                items(caracteristicasEntrenamientos) { entrenamiento ->
                    EntrenamientosCardExpandible(
                        caracteristicas = entrenamiento,
                        onSeleccionar = { categoria, subcategoria ->
                            categoriaSeleccionada = categoria
                            subcategoriaSeleccionada = subcategoria
                        }
                    )
                }
            }

            item {
                Column {
                    ControlConBoton(
                        caracteristica = "series",
                        titulo = "Número de series",
                        valor = series.value,
                        onValueChange = { series.value = it },
                        showInput = showSeriesInput,
                        onToggleInput = { showSeriesInput = !showSeriesInput }
                    )

                    ControlConBoton(
                        caracteristica = "repeticiones",
                        titulo = "Número de repeticiones",
                        valor = repeticiones.value,
                        onValueChange = { repeticiones.value = it },
                        showInput = showRepeticionesInput,
                        onToggleInput = { showRepeticionesInput = !showRepeticionesInput }
                    )

                    ControlConBoton(
                        caracteristica = "segundos",
                        titulo = "Descanso entre series",
                        valor = descanso.value,
                        onValueChange = { descanso.value = it },
                        showInput = showDescansoInput,
                        onToggleInput = { showDescansoInput = !showDescansoInput }
                    )
                }
            }

            item {
                SelectorDiasSemana(
                    diasIniciales = diasSeleccionados,
                    onDiasSeleccionadosChange = { nuevosDias ->
                        diasSeleccionados = nuevosDias
                        listaEjercicios.replaceAll { ejercicio ->
                            ejercicio.toMutableMap().apply { this["dias"] = nuevosDias.toList() }
                        }
                    }
                )
            }


            item {
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Categoría: ",
//                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = categoriaSeleccionada ?: "No seleccionada",
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Subcategoría: ",
//                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = subcategoriaSeleccionada ?: "No seleccionada",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                RutinasArchivadas(listaEjercicios)
            }
        }
    }

}

@Composable
fun EntrenamientosCardExpandible(
    caracteristicas: DataClassCaracteristicasEntrenamientos,
    onSeleccionar: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 80.dp),
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(containerColor = Color(212, 172, 13 )),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    text = caracteristicas.nombre ?: "Nombre desconocido",
                    color = Color.Black
                )
            }

            if (expanded) {
                caracteristicas.subcategorias?.forEach { subcategoria ->
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 80.dp).height(65.dp).padding(top = 10.dp),
                        onClick = {
                            onSeleccionar(caracteristicas.nombre ?: "Desconocido", subcategoria)
                            expanded = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(249, 231, 159 )),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = subcategoria,
                            color = Color.Black,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectorDiasSemana(
    diasIniciales: Set<String> = emptySet(),
    onDiasSeleccionadosChange: (Set<String>) -> Unit
) {
    val diasSemana = listOf("L", "M", "X", "J", "V", "S", "D")
    var diasSeleccionados by remember { mutableStateOf(diasIniciales) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Días para entrenar",
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                diasSemana.forEach { dia ->
                    val estaSeleccionado = diasSeleccionados.contains(dia)

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                color = if (estaSeleccionado) Color(0xFF4CAF50) else Color.Gray,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                diasSeleccionados = if (estaSeleccionado)
                                    diasSeleccionados - dia
                                else
                                    diasSeleccionados + dia

                                onDiasSeleccionadosChange(diasSeleccionados)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dia,
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun NumberPickerView(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                this.value = value
                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            }
        },
        update = {
            it.minValue = range.first
            it.maxValue = range.last
            it.value = value
        }
    )
}

@Composable
fun ControlConBoton(
    caracteristica: String,
    titulo: String,
    valor: Int,
    onValueChange: (Int) -> Unit,
    showInput: Boolean,
    onToggleInput: () -> Unit
) {
    var mensajeGuardado by remember { mutableStateOf<String?>(null) }
    val rango = when (caracteristica) {
        "segundos" -> 1..300
        else -> 1..100
    }

    LaunchedEffect(mensajeGuardado) {
        if (mensajeGuardado != null) {
            kotlinx.coroutines.delay(2000)
            mensajeGuardado = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo, color = Color.Gray)

        Spacer(modifier = Modifier.height(4.dp))

        if (showInput) {
            NumberPickerView(
                value = valor,
                onValueChange = onValueChange,
                range = rango
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (showInput) {
                    mensajeGuardado = "$titulo guardado correctamente"
                }
                onToggleInput()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (showInput) Color(0xFF4CAF50) else Color(95, 95, 95),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 70.dp)
        ) {
            Text(text = if (showInput) "Guardar" else "$valor $caracteristica")
        }

        if (mensajeGuardado != null) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = mensajeGuardado!!,
                color = Color(0xFF4CAF50),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun RutinasArchivadas(listaEjercicios: List<Map<String, Any>>) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val desplazamiento = 0.dp
    var offsetY by remember { mutableStateOf((-210).dp) }
    var expanded by remember { mutableStateOf(false) }
    val offsetAnimado by animateDpAsState(targetValue = offsetY, label = "")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .offset(y = -offsetAnimado)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color(44, 44, 44))
            .clickable {
                expanded = !expanded
                offsetY = if (expanded) desplazamiento else (-210).dp
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Text("Rutinas archivadas", color = Color.White)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .weight(1f)
        ) {
            items(listaEjercicios) { rutina ->
                RutinaCardArchivada(rutina)
            }
        }
    }
}


@Composable
fun RutinaCardArchivada(rutina: Map<String, Any>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, Color(145, 145, 145), RoundedCornerShape(15.dp))
            .clickable {
                expanded = !expanded
            }
            .animateContentSize(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(240, 240, 240))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Text(
                    "${rutina["categoria"]}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (expanded) {
                Text("Categoría: ${rutina["categoria"]}", color = Color.Black)
                Text("Subcategoría: ${rutina["subcategoria"]}", color = Color.Black)
                Text("Número de series:  ${rutina["series"]}", color = Color.Black)
                Text("Numero de repeticiones: ${rutina["repeticiones"]}", color = Color.Black)
                Text("Descanso entre series: ${rutina["descanso"]}", color = Color.Black)
                Text(
                    "Días: ${(rutina["dias"] as? List<*>)?.joinToString(", ") ?: ""}",
                    color = Color.Black
                )
            }
        }
    }
}


//enum class ButtonState {
//    IDLE, LOADING, SUCCESS, ERROR
//}
//
//@Composable
//fun AnimatedSaveButton(
//    listaEjercicios: List<Map<String, Any>>,
//    userId: String,
//    nombreEntrenamiento: String,
//    diasSeleccionados: Set<String>,
//    descanso: Int,
//    repeticiones: Int,
//    series: Int,
//    bloqueTimestamp: Long,
//    viewModelCategoria: CaracteristicasEntrenamientoViewModel,
//    navController: NavController
//) {
//    var buttonState by remember { mutableStateOf(ButtonState.IDLE) }
//    val transition = updateTransition(targetState = buttonState, label = "buttonTransition")
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//
//    // Animación de tamaño
//    val buttonSize by transition.animateDp(
//        transitionSpec = { tween(durationMillis = 200) },
//        label = "buttonSize"
//    ) { state ->
//        when (state) {
//            ButtonState.LOADING -> 48.dp
//            else -> 40.dp
//        }
//    }
//
//    // Animación de color de fondo
//    val buttonColor by transition.animateColor(
//        transitionSpec = { tween(durationMillis = 300) },
//        label = "buttonColor"
//    ) { state ->
//        when (state) {
//            ButtonState.IDLE -> Color.White
//            ButtonState.LOADING -> Color(0xFF2196F3) // Azul
//            ButtonState.SUCCESS -> Color(0xFF4CAF50) // Verde
//            ButtonState.ERROR -> Color(0xFFF44336) // Rojo
//        }
//    }
//
//    // Animación de rotación para el ícono de carga
//    val infiniteTransition = rememberInfiniteTransition()
//    val rotation by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 360f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ),
//        label = "loadingRotation"
//    )
//
//    Button(
//        onClick = {
//            if (listaEjercicios.isNotEmpty()) {
//                buttonState = ButtonState.LOADING
//
//                scope.launch {
//                    try {
//                        listaEjercicios.forEachIndexed { index, rutina ->
//                            val categoria = rutina["categoria"] as String
//                            val subcategoria = rutina["subcategoria"] as String
//
//                            viewModelCategoria.guardarRutinaEnFirestore(
//                                userId,
//                                categoria,
//                                subcategoria,
//                                nombreEntrenamiento,
//                                diasSeleccionados.toList(),
//                                descanso,
//                                repeticiones,
//                                series,
//                                bloqueTimestamp,
//                                index
//                            )
//                        }
//
//                        buttonState = ButtonState.SUCCESS
//                        delay(500) // Muestra el estado de éxito brevemente
//
//                        Toast.makeText(
//                            context,
//                            "Rutinas guardadas en Firebase",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        navController.navigate("tabRowPantallas")
//                    } catch (e: Exception) {
//                        buttonState = ButtonState.ERROR
//                        Toast.makeText(
//                            context,
//                            "Error al guardar: ${e.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        delay(1000)
//                        buttonState = ButtonState.IDLE
//                    }
//                }
//            } else {
//                buttonState = ButtonState.ERROR
//                Toast.makeText(
//                    context,
//                    "No hay rutinas para guardar",
//                    Toast.LENGTH_SHORT
//                ).show()
//                scope.launch {
//                    delay(1000)
//                    buttonState = ButtonState.IDLE
//                }
//            }
//        },
//        colors = ButtonDefaults.buttonColors(
//            containerColor = buttonColor,
//            contentColor = when (buttonState) {
//                ButtonState.IDLE -> Color.Black
//                else -> Color.White
//            }
//        ),
//        modifier = Modifier
//            .padding(bottom = 15.dp, top = 15.dp, end = 15.dp)
//            .height(buttonSize)
//            .width(if (buttonState == ButtonState.LOADING) buttonSize else 120.dp),
//        shape = RoundedCornerShape(5.dp),
//        enabled = buttonState != ButtonState.LOADING
//    ) {
//        when (buttonState) {
//            ButtonState.IDLE -> Text("Guardar", fontSize = 16.sp)
//            ButtonState.LOADING -> {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    color = Color.White,
//                    strokeWidth = 3.dp
//                )
//            }
//            ButtonState.SUCCESS -> Icon(
//                imageVector = Icons.Default.Check,
//                contentDescription = "Éxito",
//                tint = Color.White
//            )
//            ButtonState.ERROR -> Icon(
//                imageVector = Icons.Default.Error,
//                contentDescription = "Error",
//                tint = Color.White
//            )
//        }
//    }
//}
