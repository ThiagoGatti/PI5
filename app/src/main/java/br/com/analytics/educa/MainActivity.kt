package br.com.analytics.educa

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.analytics.educa.ui.route.Route
import br.com.analytics.educa.ui.screen.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Route.initialScreen) {
                // Tela inicial
                composable(route = Route.initialScreen) {
                    InitialScreen(
                        navigateToLogin = { navController.navigate(Route.login) }
                    )
                }

                // Tela de login
                composable(route = Route.login) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(Route.userVerification)
                        }
                    )
                }

                // Tela de verificação de usuário
                composable(route = Route.userVerification) {
                    UserVerification(
                        navigateToMenu = { menuRoute ->
                            navController.navigate(menuRoute)
                        }
                    )
                }

                // Menu do aluno
                composable(route = Route.menuAluno) {
                    MenuAluno(
                        navigateToAnswerForm = { navController.navigate(Route.answerFormAlunoAutonomia) },
                        navigateToScreenFour = { navController.navigate(Route.screenFour) },
                        navigateToScreenFive = { navController.navigate(Route.screenFive) },
                        navigateBack = { navController.popBackStack() }
                    )
                }

                // Tela de responder formulários
                composable(route = Route.answerFormAlunoAutonomia) {
                    AnswerFormAlunoAutonomia(
                        navigateBack = { navController.popBackStack() }
                    )
                }

                // Outras telas podem ser adicionadas aqui conforme necessário
            }
        }
    }
}
