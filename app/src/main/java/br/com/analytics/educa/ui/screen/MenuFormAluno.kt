package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuFormAluno(
    navigateToFormAutonomia: () -> Unit,
    navigateToFormClimaEscolar: () -> Unit,
    navigateToFormQualidadeEnsino: () -> Unit,
    navigateToFormInfraestrutura: () -> Unit,
    navigateToFormGestao: () -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título do menu
        Text(
            text = "Formulários Disponíveis",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Botões para cada formulário
        Button(
            onClick = navigateToFormAutonomia,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Formulário - Autonomia e Protagonismo")
        }

        Button(
            onClick = navigateToFormClimaEscolar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Formulário - Clima Escolar")
        }

        Button(
            onClick = navigateToFormQualidadeEnsino,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Formulário - Qualidade do Ensino")
        }

        Button(
            onClick = navigateToFormInfraestrutura,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Formulário - Infraestrutura")
        }

        Button(
            onClick = navigateToFormGestao,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Formulário - Gestão")
        }

        // Botão para voltar
        Button(
            onClick = navigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Voltar")
        }
    }
}

