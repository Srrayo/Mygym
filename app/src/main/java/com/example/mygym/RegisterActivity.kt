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
                            //pecho
                            "Press de banca plano con barra",
                            "Press de banca inclinado con barra",
                            "Press de banca declinado con barra",
                            "Press de banca con agarre cerrado",
                            "Press de banca con agarre inverso",
                            "Press de banca con cadena",
                            "Press de banca con bandas elásticas",
                            "Press de banca con pausa",
                            "Press de banca con tempos",
                            "Press de banca Spoto",
                            "Press de banca con fat grip",
                            "Press de banca con mancuernas",
                            "Press inclinado con mancuernas",
                            "Press declinado con mancuernas",
                            "Press con mancuernas en supinación",
                            "Press alternado con mancuernas",
                            "Press con mancuernas en floor press",
                            "Aperturas con mancuernas plano",
                            "Aperturas inclinadas con mancuernas",
                            "Aperturas declinadas con mancuernas",
                            "Pull-over con mancuerna",
                            "Flyes con mancuernas en banco plano",
                            "Flyes con mancuernas en banco inclinado",
                            "Press de pecho en máquina Hammer Strength",
                            "Press vertical en máquina",
                            "Press inclinado en máquina",
                            "Peck deck (máquina mariposa)",
                            "Cruces en poleas altas",
                            "Cruces en poleas bajas",
                            "Cruces en poleas con un brazo",
                            "Press de pecho en polea baja",
                            "Press inclinado en polea",
                            "Aperturas en polea alta",
                            "Aperturas en polea baja",
                            "Flexiones tradicionales",
                            "Flexiones diamante",
                            "Flexiones con pies elevados",
                            "Flexiones con manos elevadas",
                            "Flexiones archer",
                            "Flexiones con palmada",
                            "Flexiones pliométricas",
                            "Flexiones con rotación",
                            "Flexiones con banda elástica",
                            "Dips en paralelas",
                            "Dips con peso añadido",
                            "Dips entre bancos",
                            "Press con kettlebells",
                            "Floor press con kettlebells",
                            "Flyes con kettlebells",
                            "Press con balón medicinal",
                            "Lanzamientos de pecho con balón",
                            "Press de pecho con bandas de resistencia",
                            "Aperturas con bandas de resistencia",
                            "Press isométrico en rack",
                            "Press de pecho con landmine",
                            "Press de pecho con TRX",
                            "Flexiones en anillas",
                            "Press de pecho en máquina Smith",
                            "Press inclinado en máquina Smith",
                            "Press declinado en máquina Smith",
                            //pierna
                            "Sentadillas con barra",
                            "Sentadillas frontales",
                            "Sentadillas búlgaras",
                            "Sentadillas goblet",
                            "Sentadillas sumo",
                            "Sentadillas hack",
                            "Prensa de piernas",
                            "Extensiones de cuadriceps",
                            "Zancadas con barra",
                            "Zancadas con mancuernas",
                            "Zancadas caminando",
                            "Zancadas laterales",
                            "Step-ups con peso",
                            "Pistol squats",
                            "Peso muerto rumano",
                            "Peso muerto convencional",
                            "Peso muerto sumo",
                            "Good mornings",
                            "Curl femoral tumbado",
                            "Curl femoral sentado",
                            "Curl femoral de pie",
                            "Hiperextensiones",
                            "Hip thrust con barra",
                            "Puente de glúteos",
                            "Patada de glúteos en polea",
                            "Abducción de cadera",
                            "Elevaciones de gemelos de pie",
                            "Elevaciones de gemelos sentado",
                            "Elevaciones en prensa",
                            "Apertura de piernas en máquina",
                            "Cierre de piernas en máquina",
                            "Sentadilla Jefferson",
                            "Prensa con pies altos",
                            "Prensa con pies bajos",
                            "Prensa con pies estrechos",
                            "Prensa con pies anchos",
                            "Extensiones a una pierna",
                            "Curl femoral a una pierna",
                            "Peso muerto con mancuernas",
                            "Peso muerto con kettlebell",
                            "Sentadilla con salto",
                            "Zancadas con salto",
                            "Step-ups con salto",
                            "Bulgarian split squat",
                            "Box jumps",
                            "Sentadilla con banda",
                            "Paseo del cangrejo",
                            "Paseo lateral con banda",
                            "Elevaciones de talones",
                            "Sentadilla overhead",
                            "Sentadilla con pausa",
                            "Sentadilla con cadena",
                            "Sentadilla con banda elástica",
                            "Prensa inclinada",
                            "Prensa vertical",
                            "Extensión de cuadriceps con banda",
                            "Curl nordico",
                            "Sentadilla sissy",
                            "Sentadilla en máquina Smith",
                            "Prensa en máquina Hammer",
                            "Step-ups laterales",
                            "Zancadas con rotación",
                            "Peso muerto deficitario",
                            "Peso muerto con pausa",
                            "Good mornings con mancuernas",
                             //espalda
                            "Dominadas pronas (agarre ancho)",
                            "Dominadas supinas (agarre estrecho)",
                            "Dominadas neutras",
                            "Dominadas con peso",
                            "Jalón al pecho con polea alta",
                            "Jalón al pecho con agarre supino",
                            "Jalón al pecho con agarre neutro",
                            "Jalón al pecho unilateral",
                            "Remo con barra",
                            "Remo con barra en pronación",
                            "Remo con barra en supinación",
                            "Remo Pendlay",
                            "Remo con mancuernas",
                            "Remo con mancuerna a una mano",
                            "Remo invertido en barra",
                            "Remo en máquina sentado",
                            "Remo con polea baja",
                            "Remo con agarre en V",
                            "Remo T-bar",
                            "Remo en máquina Hammer",
                            "Peso muerto convencional",
                            "Peso muerto sumo",
                            "Peso muerto rumano",
                            "Peso muerto con mancuernas",
                            "Hiperextensiones",
                            "Pull-over con barra",
                            "Pull-over con mancuerna",
                            "Pull-over en polea",
                            "Face pull con cuerda",
                            "Encogimientos con barra",
                            "Encogimientos con mancuernas",
                            "Encogimientos en máquina Smith",
                            "Jalón tras nuca",
                            "Remo en máquina con apoyo pectoral",
                            "Remo con banda elástica",
                            "Remo con TRX",
                            "Superman en el suelo",
                            "Elevaciones de tronco en hiperextensión",
                            "Remo invertido en anillas",
                            "Dominadas explosivas",
                            "Dominadas con agarre mixto",
                            "Jalón al pecho con agarre invertido",
                            "Remo con kettlebell",
                            "Peso muerto con trampa",
                            "Remo con cadena",
                            "Remo con banda en pie",
                            "Pull-down con agarre de martillo",
                            "Remo a un brazo en polea baja",
                            "Remo giratorio con mancuerna",
                            "Remo en máquina de palanca",
                            "Jalón al pecho con parada isométrica",
                            "Remo con agarre giratorio",
                            "Dominadas con pausa",
                            "Remo con agarre alternado",
                            "Pull-over con kettlebell",
                            "Jalón al pecho con sobrecarga excéntrica",
                            "Remo con agarre offset",
                            "Peso muerto con banda elástica",
                            "Remo con agarre en diamante",
                            "Pull-down unilateral con rotación",
                             //brazo
                            "Curl de bíceps con barra",
                            "Curl de bíceps con barra en agarre ancho",
                            "Curl de bíceps con barra en agarre estrecho",
                            "Curl de bíceps con mancuernas",
                            "Curl martillo con mancuernas",
                            "Curl de bíceps concentrado",
                            "Curl de bíceps en banco Scott",
                            "Curl de bíceps con polea baja",
                            "Curl de bíceps en máquina",
                            "Curl de bíceps con banda elástica",
                            "Curl de bíceps con agarre invertido",
                            "Curl de bíceps con cuerda en polea",
                            "Curl de bíceps a un brazo con mancuerna",
                            "Curl de bíceps en predicador con mancuerna",
                            "Curl de bíceps con agarre neutro",
                            "Press francés con barra",
                            "Fondos en paralelas (tríceps)",
                            "Extensiones de tríceps con mancuerna",
                            "Extensiones de tríceps en polea alta",
                            "Extensiones de tríceps con cuerda",
                            "Fondos en banco (tríceps)",
                            "Press francés con mancuerna",
                            "Patada de tríceps con mancuerna",
                            "Extensiones de tríceps tras nuca con mancuerna",
                            "Extensiones de tríceps en máquina",
                            "Press cerrado con barra (tríceps)",
                            "Extensiones de tríceps con banda elástica",
                            "Extensiones de tríceps con kettlebell",
                            "Flexiones diamante (tríceps)",
                            "Extensiones de tríceps a un brazo en polea",
                            "Extensiones de tríceps con agarre invertido",
                            "Press francés inclinado",
                            "Extensiones de tríceps con agarre neutro",
                            "Extensiones de tríceps en banco declinado",
                            "Flexiones de muñeca (antebrazos)",
                            "Extensiones de muñeca (antebrazos)",
                            "Curl de muñeca con barra",
                            "Curl de muñeca invertido con barra",
                            "Roller de antebrazo con pesa",
                            "Agarres estáticos con discos",
                            "Flexiones de dedos con banda",
                            "Pronación y supinación con mancuerna",
                            "Martillo con rotación (antebrazos)",
                            "Encogimientos con barra (trapecio)",
                            "Encogimientos con mancuernas (trapecio)",
                            "Dominadas con agarre invertido (bíceps)",
                            "Curl Zottman (bíceps y antebrazo)",
                            "Remo al mentón con agarre cerrado (trapecio/deltoides)",
                             //abdomen
                            "Crunch abdominal",
                            "Crunch con piernas elevadas",
                            "Crunch inverso",
                            "Crunch con rotación (oblícuos)",
                            "Crunch en máquina",
                            "Crunch con polea alta",
                            "Crunch en pelota suiza",
                            "Crunch con peso en el pecho",
                            "Crunch tocando tobillos",
                            "Crunch bicicleta",
                            "Plancha frontal (isométrico)",
                            "Plancha lateral (oblícuos)",
                            "Plancha con elevación de brazo/pierna",
                            "Plancha con rodillas al codo",
                            "Plancha dinámica (rocking plank)",
                            "Plancha con toques de hombro",
                            "Elevación de piernas colgado",
                            "Elevación de piernas en banco",
                            "Elevación de rodillas en suspensión",
                            "Elevación de piernas con peso",
                            "Toes to bar (pies a la barra)",
                            "Russian twists con peso",
                            "Russian twists en el suelo",
                            "Russian twists en banco inclinado",
                            "Giros rusos con mancuerna",
                            "Abdominales en V (V-ups)",
                            "Abdominales en L (L-sit)",
                            "Abdominales tijera",
                            "Abdominales navaja",
                            "Abdominales dragon flag",
                            "Abdominales con rueda (ab wheel)",
                            "Abdominales en máquina de torsión",
                            "Abdominales con banda elástica",
                            "Abdominales con TRX",
                            "Abdominales con balón medicinal",
                            "Abdominales con soga en polea",
                            "Abdominales con patada de escalador (mountain climbers)",
                            "Abdominales con salto (jackknife)",
                            "Abdominales con piernas en 90°",
                            "Abdominales isométricos (vacío abdominal)",
                            "Abdominales con contracción estática",
                            "Abdominales con giro de cadera",
                            "Abdominales con levantamiento de pelvis",
                            "Abdominales en silla romana",
                            "Abdominales con press pallof (antirotación)",
                            "Abdominales con kettlebell (windmill)",
                            "Abdominales con levantamiento de piernas en paralelas",
                            "Abdominales con giro de medicina ball",
                            "Abdominales con flexión lateral con mancuerna",
                            "Abdominales con patada de mula (donkey kicks)",
                            "Abdominales con resistencia de compañero",
                            //hombro
                            "Press militar con barra",
                            "Press militar con mancuernas",
                            "Press Arnold (press rotacional)",
                            "Press de hombros en máquina",
                            "Press militar tras nuca",
                            "Press de hombros con kettlebell",
                            "Press de hombros con banda elástica",
                            "Press de hombros en multipower",
                            "Elevaciones frontales con mancuernas",
                            "Elevaciones frontales con barra",
                            "Elevaciones frontales con disco",
                            "Elevaciones frontales en polea baja",
                            "Elevaciones laterales con mancuernas",
                            "Elevaciones laterales en polea",
                            "Elevaciones laterales inclinadas",
                            "Elevaciones laterales con banda elástica",
                            "Elevaciones laterales en máquina",
                            "Face pull con cuerda",
                            "Face pull con banda elástica",
                            "Remo al mentón con barra",
                            "Remo al mentón con mancuernas",
                            "Remo al mentón en polea",
                            "Remo al mentón con agarre ancho",
                            "Vuelos posteriores en máquina",
                            "Vuelos posteriores con mancuernas",
                            "Vuelos posteriores en polea",
                            "Vuelos posteriores en banco inclinado",
                            "Vuelos posteriores con banda elástica",
                            "Rotaciones externas con mancuerna",
                            "Rotaciones internas con banda",
                            "Rotaciones externas en polea",
                            "Encogimientos de hombros con barra",
                            "Encogimientos de hombros con mancuernas",
                            "Encogimientos de hombros en máquina",
                            "Press de hombros con agarre martillo",
                            "Press de hombros a un brazo",
                            "Press de hombros en handstand (parado de manos)",
                            "Press de hombros con cadena",
                            "Elevaciones en Y con mancuernas",
                            "Elevaciones en T con mancuernas",
                            "Elevaciones en L con mancuernas",
                            "Press Z (press de hombros con rotación)",
                            "Press de hombros con pausa isométrica",
                            "Press de hombros con sobrecarga excéntrica",
                            "Press de hombros con agarre alternado",
                            "Elevaciones laterales con giro",
                            "Elevaciones frontales con giro",
                            "Press de hombros con agarre neutro",
                            "Press de hombros con kettlebell a un brazo",
                            "Press de hombros en máquina Hammer",
                            "Press de hombros con agarre offset"
                        ),
                        "Cardio" to listOf("Cinta", "Elíptica", "Bicicleta", "Saltos"),
                        "Flexibilidad" to listOf(
                            "Yoga",
                            "Estiramientos dinámicos",
                            "Estiramientos estáticos"
                        ),
                        "Fuerza" to listOf("Peso libre", "Máquinas", "Calistenia")
                    ),
                    "Series" to 0,
                    "Repeticiones" to 0,
                    "Descanso" to 0,
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


