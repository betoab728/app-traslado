package com.grupoct.gestionalmacen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.grupoct.gestionalmacen.data.AuthService
import com.grupoct.gestionalmacen.ui.login.LoginScreen
import com.grupoct.gestionalmacen.ui.login.MainApp
import com.grupoct.gestionalmacen.viewmodel.LoginViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class MainActivity : ComponentActivity() {

    private val authService = AuthService()
    private val loginViewModel = LoginViewModel(authService)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
           // App()
          MainApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}