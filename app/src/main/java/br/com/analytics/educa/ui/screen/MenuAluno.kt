package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuAluno (
    navigateToAnswerForm: () -> Unit,
    navigateBack: () -> Unit,
    navigateToScreenFour: () -> Unit,
    navigateToScreenFive: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Menu")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToAnswerForm) { Text("Formulários Avaliativos") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreenFour) { Text("Visualizar Resultados de Formulários") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreenFive) { Text("Dados Pessoais") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateBack) { Text("Voltar") }
    }
}
