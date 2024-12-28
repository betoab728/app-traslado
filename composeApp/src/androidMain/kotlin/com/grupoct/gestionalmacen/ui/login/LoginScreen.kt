package com.grupoct.gestionalmacen.ui.login
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import com.grupoct.gestionalmacen.viewmodel.LoginViewModel
import com.grupoct.gestionalmacen.viewmodel.LoginState
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.grupoct.gestionalmacen.R


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit // Callback que recibe el token tras el login exitoso
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    // Navegar cuando el inicio de sesión sea exitoso
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            val token = (loginState as LoginState.Success).token
            onLoginSuccess(token) // Pasar el token a la siguiente pantalla o lógica
        }
    }

    // Mostrar error en Snackbar
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = (loginState as LoginState.Error).message
            )
        }
    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF336DE1),
                            Color(0xFFEF2F2F)
                        )
                    )
                )
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                //icono de login
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Login",
                    modifier = Modifier.size(200.dp)
                )
                //espacio entre el icono y el texto
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.h5.copy(color = Color.White),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = MaterialTheme.colors.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colors.onSurface
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = MaterialTheme.colors.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colors.onSurface
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(modifier = Modifier.height(24.dp))
                val isFormValid = username.isNotBlank() && password.isNotBlank()
                Button(
                    onClick = { viewModel.login(username, password) },
                    enabled = isFormValid && loginState !is LoginState.Loading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White
                    )
                ) {
                    if (loginState is LoginState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Ingresar", color = Color.Blue)
                    }
                }
            }
        }
    }

}
