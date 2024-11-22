package br.com.analytics.educa

import LoginScreen
import android.os.Bundle
import kotlinx.coroutines.launch
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.analytics.educa.ui.theme.EducaAnalyticsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.sql.SQLException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EducaAnalyticsTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Route.screenOne) {
                    composable(route = Route.screenOne) {
                        ScreenOne(
                            navigateToScreenTwo = { navController.navigate(Route.screenTwo) }
                        )
                    }
                    composable(route = Route.screenTwo) {
                        ScreenTwo(
                            navigateToScreenThree = { navController.navigate(Route.screenThree) },
                            navigateToScreenFour = { navController.navigate(Route.screenFour) },
                            navigateToScreenFive = { navController.navigate(Route.screenFive) },
                            navigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = Route.screenThree) {
                        ScreenThree(
                            navigateBack = { navController.popBackStack() },
                        )
                    }
                    composable(route = Route.screenFour) {
                        ScreenFour(
                            navigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = Route.screenFive) {
                        ScreenFive(
                            navigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

object Route {
    const val screenOne = "screenOne"
    const val screenTwo = "screenTwo"
    const val screenThree = "screenThree"
    const val screenFour = "screenFour"
    const val screenFive = "screenFive"
}

@Composable
fun ScreenOne(
    navigateToScreenTwo: () -> Unit
) {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TELA PROVISÓRIA PARA O MENU (cada tipo de usuario terá um menu diferente)",
            )
            Button(onClick = navigateToScreenTwo) {
                Text(
                    text = "IR AO MENU",
                )
            }
        }
    } else {

        LoginScreen(
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}

@Composable
fun ScreenTwo(
    navigateToScreenThree: () -> Unit,
    navigateBack: () -> Unit,
    navigateToScreenFour: () -> Unit,
    navigateToScreenFive: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreenThree) {
            Text("Formularios Avaliativos")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreenFour) { // Botão para a tela 4
            Text("Visualizar Resultados de Formularios")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreenFive) { // Botão para a tela 5
            Text("Opção 3")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateBack) {
            Text("Voltar")
        }
    }
}

@Composable
fun ScreenThree(
    navigateBack: () -> Unit
) {
    var selectedRating by remember { mutableStateOf(5) }
    val context = LocalContext.current
    val databaseHelper = remember { DatabaseHelper(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... outros componentes da tela
        Text("De 0 a 10, o Quando você, como aluno, se sente a vontade nas aulas")

        Slider(
            value = selectedRating.toFloat(),
            onValueChange = { selectedRating = it.toInt() },
            valueRange = 1f..10f,
            steps = 9
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val connection = databaseHelper.connect()
                if (connection != null) {
                    try {
                        val statement = connection.createStatement()
                        val query = "INSERT INTO FormularioResultados (Nota) VALUES ($selectedRating)"
                        statement.executeUpdate(query)
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    } finally {
                        databaseHelper.closeConnection(connection)
                    }
                }
            }
            navigateBack()
        }) {
            Text("Salvar e Voltar")
        }
    }
}

@Composable
fun ScreenFour(navigateBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = navigateBack) {
            Text("Voltar")
        }
    }
}

@Composable
fun ScreenFive(navigateBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = navigateBack) {
            Text("Voltar")
        }
    }
}