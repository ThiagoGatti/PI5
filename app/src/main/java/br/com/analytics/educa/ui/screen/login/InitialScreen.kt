package br.com.analytics.educa.ui.screen.login

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.R

@Composable
fun InitialScreen(
    navigateToLogin: () -> Unit
) {
    // Animação da logo
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7)) //degrade roxo de fundo
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.educa_analytics_logo), // LOGO
                contentDescription = "Logo",
                modifier = Modifier
                    .size(500.dp)
                    .offset(y = offsetY.dp) // SOBE E DESCE SOBE E DESCE FUNCIONOU
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )

            // Título
            Text(
                text = "Bem-vindo",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 70.dp),
                fontSize = 30.sp
            )

            // Texto
            Text(
                text = "Sua jornada de aprendizado começa aqui.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 65.dp),
                fontSize = 18.sp
            )

            // Botao de Login
            Button(
                onClick = navigateToLogin,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),

                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5D145B), // Cor de Fundo
                    contentColor = Color.White // Cor do Texto
                )

            ) {
                Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Ícone de login",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
                Text(

                    text = "Logar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }
    }
}