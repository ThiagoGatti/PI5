package br.com.analytics.educa.ui.screen

import LoginScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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
            Text(text = "Tela provisória para o menu (cada tipo de usuário terá um menu diferente)")
            Button(onClick = navigateToScreenTwo) {
                Text("Ir ao Menu")
            }
        }
    } else {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    }
}