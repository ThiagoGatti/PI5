package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.analytics.educa.ui.route.Route
import kotlinx.coroutines.delay

@Composable
fun UserVerification(
    navigateToMenu: (String) -> Unit
) {
    // Simulação de carregamento e navegação
    LaunchedEffect(Unit) {
        delay(500) // Simula um delay de 2 segundos para o loading

        // Navega para a próxima tela diretamente, independente da verificação
        navigateToMenu(Route.menuAluno)
    }

    // Tela de loading enquanto navega
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
        Text("Verificando tipo de usuário...", Modifier.align(Alignment.Center))
    }
}
