package br.com.analytics.educa

import LoginScreen
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
                        navigateToMenuFormAluno = { navController.navigate(Route.menuAlunoForm) },
                        navigateBack = { navController.popBackStack() }
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

                composable(route = Route.formAlunoConsideracoesFinais) {
                    Formulario(
                        tipoUsuario = "Aluno",
                        form = "Considerações Finais",
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
                        navigateToFormConsideracoesFinais = { navController.navigate(Route.formAlunoConsideracoesFinais) },
                        navigateBack = { navController.popBackStack() }
                    )
                }

            }
        }
    }
}