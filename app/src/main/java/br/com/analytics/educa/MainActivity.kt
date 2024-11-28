package br.com.analytics.educa

import LoginScreen
import UserVerification
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.analytics.educa.ui.forms.Formulario
import br.com.analytics.educa.ui.menu.Menu
import br.com.analytics.educa.ui.menu.MenuForm
import br.com.analytics.educa.ui.route.Route
import br.com.analytics.educa.ui.screen.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Route.initialScreen) {
                composable(route = Route.initialScreen) {
                    InitialScreen(
                        navigateToLogin = { navController.navigate(Route.login) }
                    )
                }

                composable(route = Route.login) {
                    LoginScreen(
                        onLoginSubmit = { username, password ->
                            navController.navigate("${Route.userVerification}/$username/$password")
                        }
                    )
                }

                composable(route = "${Route.userVerification}/{username}/{password}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val password = backStackEntry.arguments?.getString("password") ?: ""
                    UserVerification(
                        username = username,
                        password = password,
                        navigateToMenu = { userType ->
                            navController.navigate("${Route.menu}/$userType")
                        },
                        navigateToLogin = { navController.popBackStack() }
                    )
                }

                composable(route = "${Route.menu}/{userType}") { backStackEntry ->
                    val userType = backStackEntry.arguments?.getString("userType").toString()
                    Menu(
                        userType = userType,
                        navigateToMenuForm = {
                            navController.navigate("${Route.menuForm}/$userType")
                        },
                        navigateToInitialScreen = {
                            navController.navigate(Route.initialScreen) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable(route = "${Route.menuForm}/{userType}") { backStackEntry ->
                    val userType = backStackEntry.arguments?.getString("userType").toString()
                    MenuForm(
                        userType = userType,
                        navigateToForm = { formName ->
                            navController.navigate("${Route.formulario}/$userType/$formName")
                        },
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(route = "${Route.formulario}/{userType}/{formName}") { backStackEntry ->
                    val userType = backStackEntry.arguments?.getString("userType").toString()
                    val formName =
                        backStackEntry.arguments?.getString("formName") ?: "Formulário Desconhecido"
                    Formulario(
                        userType = userType,
                        formName = formName,
                        navigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}