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
import br.com.analytics.educa.ui.menu.MenuAluno
import br.com.analytics.educa.ui.menu.MenuFormAluno
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

                composable(route = Route.login) {
                    LoginScreen(
                        onLoginSubmit = { username, password ->
                            navController.navigate("${Route.userVerification}/$username/$password")
                        }
                    )
                }

                // Tela de verificação de usuário
                composable(route = "${Route.userVerification}/{username}/{password}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val password = backStackEntry.arguments?.getString("password") ?: ""
                    UserVerification(
                        username = username,
                        password = password,
                        navigateToMenu = { menuRoute -> navController.navigate(menuRoute) },
                        navigateToLogin = { navController.popBackStack() }
                    )
                }

                // Menu do aluno
                composable(route = Route.menuAluno) {
                    MenuAluno(
                        navigateToMenuFormAluno = { navController.navigate(Route.menuAlunoForm) },
                        navigateToInitialScreen = {
                            navController.navigate(Route.initialScreen) {
                                // Remove todas as telas anteriores da pilha para evitar retorno
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                // Tela de responder formulários (por isso falei que acho melhor fazer um menu kkkk)
                composable(route = Route.formAlunoAutonomia) {
                    Formulario(
                        tipoUsuario = "Aluno",
                        form = "Autonomia e Protagonismo",
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(route = Route.formAlunoClimaEscolar) {
                    Formulario(
                        tipoUsuario = "Aluno",
                        form = "Clima Escolar",
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(route = Route.formAlunoQualidadeEnsino) {
                    Formulario(
                        tipoUsuario = "Aluno",
                        form = "Qualidade do Ensino",
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(route = Route.formAlunoInfraestrutura) {
                    Formulario(
                        tipoUsuario = "Aluno",
                        form = "Infraestrutura",
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(route = Route.formAlunoGestao) {
                    Formulario(
                        tipoUsuario = "Aluno",
                        form = "Gestão",
                        navigateBack = { navController.popBackStack() }
                    )
                }




                composable(route = Route.menuAlunoForm) {
                    MenuFormAluno(
                        navigateToFormAutonomia = { navController.navigate(Route.formAlunoAutonomia) },
                        navigateToFormClimaEscolar = { navController.navigate(Route.formAlunoClimaEscolar) },
                        navigateToFormQualidadeEnsino = { navController.navigate(Route.formAlunoQualidadeEnsino) },
                        navigateToFormInfraestrutura = { navController.navigate(Route.formAlunoInfraestrutura) },
                        navigateToFormGestao = { navController.navigate(Route.formAlunoGestao) },
                        navigateBack = { navController.popBackStack() }
                    )
                }

            }
        }
    }
}