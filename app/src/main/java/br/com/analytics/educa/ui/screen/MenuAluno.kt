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
    navigateToMenuFormAluno: () -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Menu")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToMenuFormAluno) { Text("Formulários Avaliativos") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateBack) { Text("Voltar") }
    }
}
