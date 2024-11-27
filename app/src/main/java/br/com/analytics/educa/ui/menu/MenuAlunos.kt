package br.com.analytics.educa.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuAluno(
    navigateToMenuFormAluno: () -> Unit,
    navigateToInitialScreen: () -> Unit // Função para navegar para a tela inicial
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Menu")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToMenuFormAluno) {
            Text("Formulários Avaliativos")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            performLogout() // Função de logout
            navigateToInitialScreen() // Volta para a tela inicial (InitialScreen)
        }) {
            Text("Deslogar")
        }
    }
}

// Função simulada para logout
fun performLogout() {
    // Limpeza de dados do usuário, token, ou sessões (dependendo da lógica da aplicação)
    println("Usuário deslogado!")
}

