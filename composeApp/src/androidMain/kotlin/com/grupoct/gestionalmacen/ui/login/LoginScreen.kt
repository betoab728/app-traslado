package com.grupoct.gestionalmacen.ui.login
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))
            val isFormValid = username.isNotBlank() && password.isNotBlank()
            Button(
                onClick = { viewModel.login(username, password) },
                enabled = isFormValid && loginState !is LoginState.Loading
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colors.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login")
                }
            }
        }
    }
}
