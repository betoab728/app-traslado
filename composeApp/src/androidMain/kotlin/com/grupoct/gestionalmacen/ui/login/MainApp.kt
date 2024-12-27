package com.grupoct.gestionalmacen.ui.login
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    val loginViewModel = remember { LoginViewModel(authService = AuthService(), sharedPreferences = sharedPreferences) }
    val warehouseViewModel = remember { WarehouseViewModel(context) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel = loginViewModel) { token ->
                navController.navigate("moveProduct?token=$token")
            }
        }
        composable(
            "moveProduct?token={token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")

            if (token.isNullOrEmpty()) {
                navController.navigate("login")
            } else {
                MoveProductScreen(
                    viewModel = warehouseViewModel,
                    token = token)
            }
        }
    }
}
