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
                            navController.navigate("${Route.menu}/$userType/$username")
                        },
                        navigateToLogin = { navController.popBackStack() }
                    )
                }

                composable(route = "${Route.menu}/{userType}/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username").toString()
                    val userType = backStackEntry.arguments?.getString("userType").toString()
                    Menu(
                        userType = userType,
                        navigateToMenuForm = {
                            navController.navigate("${Route.menuForm}/$userType/$username")
                        },
                        navigateToCharts = { navController.navigate("${Route.telaDadosEscola}/$username/$userType") },
                        navigateToInitialScreen = {
                            navController.navigate(Route.initialScreen) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        navigateToNotas = { navController.navigate("${Route.telaNotas}/$username") },
                        navigateToEnviarNotas = { navController.navigate("${Route.telaEnviarNotas}/$username") },
                        navigateToManejarUsuarios = { navController.navigate(Route.telaManejarUsuarios) },
                        navigateToDadosPessoais = { navController.navigate(Route.telaDadosPessoais) }
                    )

                }

                composable(route = "${Route.menuForm}/{userType}/{username}") { backStackEntry ->
                    val userType = backStackEntry.arguments?.getString("userType").toString()
                    val username = backStackEntry.arguments?.getString("username").toString()
                    MenuForm(
                        userType = userType,
                        username = username,
                        navigateToForm = { formName ->
                            navController.navigate("${Route.formulario}/$userType/$formName/$username")
                        },
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(route = "${Route.formulario}/{userType}/{formName}/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username").toString()
                    val userType = backStackEntry.arguments?.getString("userType").toString()
                    val formName =
                        backStackEntry.arguments?.getString("formName") ?: "Formulário Desconhecido"
                    Formulario(
                        username = username,
                        userType = userType,
                        formName = formName,
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(route = "${Route.telaEnviarNotas}/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username").toString()
                    EnviarNotasScreen(
                        navigateBack = { navController.popBackStack() },
                        turmas = listOf("Turma 1", "Turma 2", "Turma 3"),
                        getAlunosByTurma = { turma ->
                            when (turma) {
                                "Turma 1" -> listOf("Aluno 1", "Aluno 2", "Aluno 3")
                                "Turma 2" -> listOf("Aluno 4", "Aluno 5", "Aluno 6")
                                "Turma 3" -> listOf("Aluno 7", "Aluno 8", "Aluno 9")
                                else -> emptyList()
                            }
                        },
                        enviarNotaEFrequencia = { turma, aluno, nota, frequencia ->
                            // Lógica para enviar os dados
                            println("Enviando para $aluno da $turma: Nota = $nota, Frequência = $frequencia")
                        }
                    )
                }


                composable(route = "${Route.telaNotas}/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username").toString()
                    NotasScreen(
                        navigateBack = { navController.popBackStack() }
                    )
                }

<<<<<<< Updated upstream
                composable(route = "${Route.telaDadosEscola}/{username}/{userType}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username").toString()
                    TelaDadosEscola(
                        username,
                        navigateToTelaGraficoBarra = { navController.navigate(
                            "${Route.telaGraficoBarra}/$username/${backStackEntry.arguments?.getString("userType").toString()}"
                        ) },
                        navigateToTelaGraficoPizza = { navController.navigate("${Route.telaGraficoPizza}/$username") },
                        navigateBack = {
                            navController.popBackStack()
                        })
                }
=======
                composable(route = Route.telaManejarUsuarios) {
                    TelaManejarUsuarios(
                        navigateBack = { navController.popBackStack() }
                    )
                }

>>>>>>> Stashed changes

                composable(route = "${Route.telaGraficoBarra}/{username}/{userType}") { backStackEntry ->
                    TelaGraficoBarra(
                        backStackEntry.arguments?.getString("username").toString(),
                        backStackEntry.arguments?.getString("userType").toString(),
                        navigateBack = {
                            navController.popBackStack()
                        })
                }

                composable(route = "${Route.telaGraficoPizza}/{username}") { backStackEntry ->
                    TelaGraficoPizza(
                        backStackEntry.arguments?.getString("username").toString(),
                        navigateBack = {
                            navController.popBackStack()
                        })
                }
            }
        }
    }
}