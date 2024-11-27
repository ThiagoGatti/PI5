package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.ui.route.Route
import kotlinx.coroutines.delay

@Composable
fun UserVerification(
    navigateToMenu: (String) -> Unit
) {
    // Simula o delay e navega para o menu do aluno
    LaunchedEffect(Unit) {
        delay(1000) // Simula um delay de 1 segundo para o loading
        navigateToMenu(Route.menuAluno)
    }

    // Tela de verificação com fundo gradiente e loading
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Verificando tipo de usuário...",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
