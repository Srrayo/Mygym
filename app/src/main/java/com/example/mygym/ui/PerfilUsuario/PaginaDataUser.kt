package com.example.mygym.ui.PerfilUsuario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygym.model.DataClassUsuario
import com.example.mygym.ui.screens.CerrarVentana
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PaginaDataUser(navController: NavController, dataUserViewModel: DataUserViewModel) {
    // Acceder al auth para obtener el usuario logueado
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: return

    // Cargar los datos del usuario cuando la pantalla se muestra
    LaunchedEffect(userId) {
        dataUserViewModel.loadUserData(userId)
    }

    // Obtener los datos de usuario del ViewModel
    val usuario = dataUserViewModel.usuario

    // Estado para los campos editables
    // Inicializa los campos con los valores de Firebase si los datos están disponibles
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var apellidos by remember { mutableStateOf(usuario.apellidos) }
    var edad by remember { mutableStateOf(usuario.edad) }
    var peso by remember { mutableStateOf(usuario.peso) }
    var altura by remember { mutableStateOf(usuario.altura) }
    var fechaNacimiento by remember { mutableStateOf(usuario.fechaNacimiento) }

    // Estado para saber qué campo se está editando
    var campoEditando by remember { mutableStateOf<String?>(null) }

    fun saveNombre() {
        dataUserViewModel.usuario = usuario.copy(nombre = nombre)
        dataUserViewModel.updateUserProfile(userId)
        Toast.makeText(navController.context, "Nombre actualizado", Toast.LENGTH_SHORT).show()
    }

    fun saveApellidos() {
        dataUserViewModel.usuario = usuario.copy(apellidos = apellidos)
        dataUserViewModel.updateUserProfile(userId)
        Toast.makeText(navController.context, "Apellidos actualizados", Toast.LENGTH_SHORT).show()
    }

    fun saveEdad() {
        dataUserViewModel.usuario = usuario.copy(edad = edad)
        dataUserViewModel.updateUserProfile(userId)
        Toast.makeText(navController.context, "Edad actualizada", Toast.LENGTH_SHORT).show()
    }

    fun savePeso() {
        dataUserViewModel.usuario = usuario.copy(peso = peso)
        dataUserViewModel.updateUserProfile(userId)
        Toast.makeText(navController.context, "Peso actualizado", Toast.LENGTH_SHORT).show()
    }

    fun saveAltura() {
        dataUserViewModel.usuario = usuario.copy(altura = altura)
        dataUserViewModel.updateUserProfile(userId)
        Toast.makeText(navController.context, "Altura actualizada", Toast.LENGTH_SHORT).show()
    }

    fun saveFechaNacimiento() {
        dataUserViewModel.usuario = usuario.copy(fechaNacimiento = fechaNacimiento)
        dataUserViewModel.updateUserProfile(userId)
        Toast.makeText(navController.context, "Fecha de nacimiento actualizada", Toast.LENGTH_SHORT)
            .show()
    }

    @Composable
    fun CampoEditable(
        label: String,
        valor: String,
        onValorChange: (String) -> Unit,
        valorOriginal: String,
        isEditing: Boolean,
        onGuardar: () -> Unit,
        onEditarCambiar: (Boolean) -> Unit
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (!isEditing) {
                    Text(
                        text = "$label: $valorOriginal",
                        color = Color.Black,
                        style = TextStyle(fontSize = 14.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEditarCambiar(true) }
                            .padding(8.dp)
                    )
                } else {
                    TextField(
                        value = valor,
                        onValueChange = onValorChange,
                        label = { Text(label) },
                        modifier = Modifier
                            .width(300.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onGuardar()
                                onEditarCambiar(false)
                            },
                            modifier = Modifier
                                .width(100.dp) // Cambia 15.dp a un valor más razonable
                                .height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    72, 201, 176
                                ),
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text("Guardar", color = Color.White, fontSize = 12.sp)
                        }


                        Spacer(modifier = Modifier.width(5.dp))

                        Button(
                            onClick = {
                                onValorChange(valorOriginal)
                                onEditarCambiar(false)
                            },
                            modifier = Modifier
                                .width(100.dp)
                                .height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    218,
                                    89,
                                    89
                                ),
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text("Cancelar", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(32, 32, 32))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        CerrarVentana(navController)
        Spacer(modifier = Modifier.height(15.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CampoEditable(
                    label = "Nombre",
                    valor = nombre,
                    onValorChange = { nombre = it },
                    valorOriginal = usuario.nombre,
                    isEditing = campoEditando == "nombre",
                    onGuardar = { saveNombre() },
                    onEditarCambiar = { editando ->
                        campoEditando = if (editando) "nombre" else null
                    }
                )

                CampoEditable(
                    label = "Apellido",
                    valor = apellidos,
                    onValorChange = { apellidos = it },
                    valorOriginal = usuario.apellidos,
                    isEditing = campoEditando == "apellidos",
                    onGuardar = { saveApellidos() },
                    onEditarCambiar = { editando ->
                        campoEditando = if (editando) "apellidos" else null
                    }
                )

                CampoEditable(
                    label = "Edad",
                    valor = edad,
                    onValorChange = { edad = it },
                    valorOriginal = usuario.edad,
                    isEditing = campoEditando == "edad",
                    onGuardar = { saveEdad() },
                    onEditarCambiar = { editando -> campoEditando = if (editando) "edad" else null }
                )

                CampoEditable(
                    label = "Peso",
                    valor = peso,
                    onValorChange = { peso = it },
                    valorOriginal = usuario.peso,
                    isEditing = campoEditando == "peso",
                    onGuardar = { savePeso() },
                    onEditarCambiar = { editando -> campoEditando = if (editando) "peso" else null }
                )

                CampoEditable(
                    label = "Altura",
                    valor = altura,
                    onValorChange = { altura = it },
                    valorOriginal = usuario.altura,
                    isEditing = campoEditando == "altura",
                    onGuardar = { saveAltura() },
                    onEditarCambiar = { editando ->
                        campoEditando = if (editando) "altura" else null
                    }
                )

                CampoEditable(
                    label = "Fecha de Nacimiento",
                    valor = fechaNacimiento,
                    onValorChange = { fechaNacimiento = it },
                    valorOriginal = usuario.fechaNacimiento,
                    isEditing = campoEditando == "fechaNacimiento",
                    onGuardar = { saveFechaNacimiento() },
                    onEditarCambiar = { editando ->
                        campoEditando = if (editando) "fechaNacimiento" else null
                    }
                )
            }
        }
    }

}


