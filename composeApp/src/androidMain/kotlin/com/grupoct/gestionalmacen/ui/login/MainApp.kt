package com.grupoct.gestionalmacen.ui.login
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupoct.gestionalmacen.viewmodel.LoginViewModel
import com.grupoct.gestionalmacen.data.AuthService
import com.grupoct.gestionalmacen.ui.login.MoveProductScreen
import com.grupoct.gestionalmacen.viewmodel.WarehouseViewModel

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val viewModel = LoginViewModel(authService = AuthService()) // Instancia del ViewModel

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel = viewModel) { token ->
                // Navegar a la pantalla de traslado pasando el token como argumento
                navController.navigate("moveProduct?token=$token")
            }
        }
        composable(
            "moveProduct?token={token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")

            MoveProductScreen(
                token = token ?: "",
                onMove = { productCode, quantity, vitrine, destinationWarehouseId ->
                    // Lógica para mover el producto
                }
            )
        }
    }
}