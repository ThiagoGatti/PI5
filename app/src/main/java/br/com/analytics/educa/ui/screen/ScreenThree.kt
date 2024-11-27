package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ScreenThree(
    navigateBack: () -> Unit
) {
    var selectedRating by remember { mutableStateOf(5f) } // Nota padrão
    val databaseHelper = DatabaseHelper(LocalContext.current) // Instância do banco de dados

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto da pergunta
        Text("De 0 a 10, como você se sente à vontade nas aulas?")

        Spacer(modifier = Modifier.height(16.dp))

        // Slider para selecionar a nota
        Slider(
            value = selectedRating,
            onValueChange = { selectedRating = it },
            valueRange = 0f..10f, // Faixa de valores
            steps = 9 // Define os steps para 0 a 10
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para salvar no banco de dados
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val connection = databaseHelper.connect()
                if (connection != null) {
                    try {
                        val statement = connection.createStatement()
                        val query = "INSERT INTO FormularioResultados (Nota) VALUES ($selectedRating)"
                        statement.executeUpdate(query)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        databaseHelper.closeConnection(connection)
                    }
                }
            }
            navigateBack() // Voltar para a tela anterior
        }) {
            Text("Salvar e Voltar")
        }
    }
}
