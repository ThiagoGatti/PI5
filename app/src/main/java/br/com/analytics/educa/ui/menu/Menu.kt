package br.com.analytics.educa.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Menu(
    userType: String,
    navigateToMenuForm: () -> Unit,
    navigateToInitialScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Menu")
        Spacer(modifier = Modifier.height(16.dp))

        if (userType != "DIRETOR") {
            Button(
                onClick = navigateToMenuForm,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Formulários Avaliativos")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                performLogout()
                navigateToInitialScreen()
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Deslogar")
        }
    }
}

// Função simulada para logout
fun performLogout() {
    // Limpeza de dados do usuário, token ou sessões (dependendo da lógica da aplicação)
    println("Usuário deslogado!")
}