//package com.example.mygym
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.compose.rememberNavController
//import com.google.firebase.auth.FirebaseAuth
//
//class RegisterActivity : ComponentActivity() {
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        auth = FirebaseAuth.getInstance()
//
//        setContent {
//            val navController = rememberNavController()
//            RegisterScreen(auth) {
//                navigateToLogin()
//            }
//        }
//    }
//
//    private fun navigateToLogin() {
//        startActivity(Intent(this, LoginActivity::class.java))
//        finish() // Cierra la actividad actual
//    }
//}
//
//@Composable
//fun RegisterScreen(auth: FirebaseAuth, onRegisterSuccess: () -> Unit) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusManager = LocalFocusManager.current // Manejador del foco
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        /**
//         * OutlinedTextField(
//         *             value = email,
//         *             onValueChange = { email = it },
//         *             label = { Text("Email") },
//         *             keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//         *             modifier = Modifier.fillMaxWidth()
//         *         )
//         */
//
//        Image(
//            painter = painterResource(R.drawable.logob),
//            contentDescription = "logo",
//            modifier = Modifier
//                .size(120.dp)
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            modifier = Modifier.fillMaxWidth(),
//            fontSize = 15.sp,
//            text = "Registro",
//            textAlign = TextAlign.Center,
//            color = Color.White
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Card(
//            modifier = Modifier
//                .width(300.dp)
//                .height(300.dp),
//            shape = RoundedCornerShape(30.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White)
//                    .padding(30.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                TextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text(text = "Email") },
//                    modifier = Modifier
//                        .width(200.dp),
//                    singleLine = true,
//
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Black,
//                        unfocusedIndicatorColor = Color.Gray,
//                        cursorColor = Color.Black
//                    ),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Define la acción del teclado
//                    keyboardActions = KeyboardActions(
//                        onDone = {
//                            keyboardController?.hide() // Cierra el teclado
//                            focusManager.clearFocus() // Quita el foco del TextField (el cursor desaparece)
//                        }
//                    )
//                )
//
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                /**
//                 * OutlinedTextField(
//                 *             value = password,
//                 *             onValueChange = { password = it },
//                 *             label = { Text("Password") },
//                 *             visualTransformation = PasswordVisualTransformation(),
//                 *             keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                 *             modifier = Modifier.fillMaxWidth()
//                 *         )
//                 */
//
//                TextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text(text = "Password") },
//                    modifier = Modifier
//                        .width(200.dp),
//                    singleLine = true,
//
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Black,
//                        unfocusedIndicatorColor = Color.Gray,
//                        cursorColor = Color.Black
//                    ),
//                    visualTransformation = PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Define la acción del teclado
//                    keyboardActions = KeyboardActions(
//                        onDone = {
//                            keyboardController?.hide() // Cierra el teclado
//                            focusManager.clearFocus() // Quita el foco del TextField (el cursor desaparece)
//                        }
//                    )
//                )
//
//
//                Spacer(modifier = Modifier.height(30.dp))
//
//                if (isLoading) {
//                    CircularProgressIndicator()
//                } else {
//                    Button(
//                        modifier = Modifier.width(200.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.Green,
//                            contentColor = Color.White
//                        ),
//                        onClick = {
//                            if (email.isNotEmpty() && password.isNotEmpty()) {
//                                isLoading = true
//                                auth.createUserWithEmailAndPassword(email, password)
//                                    .addOnCompleteListener { task ->
//                                        isLoading = false
//                                        if (task.isSuccessful) {
//                                            Toast.makeText(
//                                                context,
//                                                "Registro completado",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                            onRegisterSuccess()
//                                        } else {
//                                            Toast.makeText(
//                                                context,
//                                                "Error de registro: ${task.exception?.message}",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    }
//                            } else {
//                                Toast.makeText(
//                                    context,
//                                    "Por favor rellene todos los campos",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        },
//                    ) {
//                        Text("Registrarse")
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextButton(
//            onClick = { onRegisterSuccess() }, colors = ButtonDefaults.textButtonColors(
//                contentColor = Color.Blue
//            )
//        )
//        {
//            Text("Estás registrado ya? Login")
//        }
//    }
//}


package com.example.mygym

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            val navController = rememberNavController()
            RegisterScreen(auth) {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

fun registrarUsuario(
    email: String,
    password: String,
    auth: FirebaseAuth,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                val db = FirebaseFirestore.getInstance()
                val userDoc = db.collection("usuarios").document(userId)

                // Datos iniciales para el usuario en Firestore
                val datosIniciales = hashMapOf(
                    "nombre" to "",
                    "apellidos" to "",
                    "edad" to "",
                    "peso" to "",
                    "altura" to "",
                    "fechaNacimiento" to "",
                    "categorias" to hashMapOf(
                        "Gimnasio" to listOf(
                            "Pecho",
                            "Pierna",
                            "Espalda",
                            "Brazo",
                            "Abdomen",
                            "Hombro"
                        ),
                        "Cardio" to listOf("Cinta", "Elíptica", "Bicicleta", "Saltos"),
                        "Flexibilidad" to listOf(
                            "Yoga",
                            "Estiramientos dinámicos",
                            "Estiramientos estáticos"
                        ),
                        "Fuerza" to listOf("Peso libre", "Máquinas", "Calistenia")
                    ),
                    "seleccionados" to listOf<String>()
                )

                // Guardar la estructura en Firestore
                userDoc.set(datosIniciales)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onError("Error al guardar datos iniciales: ${e.message}")
                    }
            } else {
                onError(task.exception?.message ?: "Error desconocido")
            }
        }
}

@Composable
fun RegisterScreen(auth: FirebaseAuth, onRegisterSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logob),
            contentDescription = "logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            text = "Registro",
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier.width(200.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password") },
                    modifier = Modifier.width(200.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.Black
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        ),
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    Toast.makeText(
                                        context,
                                        "Ingrese un correo válido",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                if (password.length < 6) {
                                    Toast.makeText(
                                        context,
                                        "La contraseña debe tener al menos 6 caracteres",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                isLoading = true
                                registrarUsuario(email, password, auth,
                                    onSuccess = {
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Registro exitoso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onRegisterSuccess()
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Por favor, complete todos los campos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                    ) {
                        Text("Registrarse")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { onRegisterSuccess() }, colors = ButtonDefaults.textButtonColors(
                contentColor = Color.Blue
            )
        ) {
            Text("¿Estás registrado ya? Login")
        }
    }
}


