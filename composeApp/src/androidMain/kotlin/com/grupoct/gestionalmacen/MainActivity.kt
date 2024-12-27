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
import android.content.Context


class MainActivity : ComponentActivity() {

    private val authService = AuthService()
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        loginViewModel = LoginViewModel(authService, sharedPreferences)

        setContent {
            MainApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}