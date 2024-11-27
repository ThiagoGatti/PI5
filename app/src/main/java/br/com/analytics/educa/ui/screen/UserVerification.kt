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

    LaunchedEffect(Unit) {
        delay(1000) // Simula um delay de 1 segundos para o loading

        // Entra na tela de aluno direto, pq nao tem verificacao de tipo ainda
        navigateToMenu(Route.menuAluno)
    }

    // Loading
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
        Text("Verificando tipo de usuário...", Modifier.align(Alignment.Center))
    }
}
