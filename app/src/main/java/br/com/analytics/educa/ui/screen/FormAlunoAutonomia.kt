package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.ui.component.StarRating

@Composable
fun FormAlunoAutonomia(
    navigateBack: () -> Unit
) {
    // Lista de perguntas
    val questions = listOf(
        "Você se sente desafiado e motivado nas aulas?",
        "Você se sente à vontade para fazer perguntas e expressar suas dúvidas?",
        "Você assume responsabilidade pelo seu processo de aprendizagem?",
        "Com que frequência você busca novas informações além do que é apresentado em sala de aula?",
        "Com que frequência você participa ativamente das discussões e atividades em sala de aula?",
        "Você se sente capaz de pensar de forma crítica e criativa?"


    )

    // Estado das respostas (1 a 10 estrelas para cada pergunta)
    val answers = remember { mutableStateListOf(*Array(questions.size) { 0 }) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF7D0CAF), Color(0xFFDAABEF))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // Título estilizado
                Text(
                    text = "Formulário - Autonomia e Protagonismo",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Exibe cada pergunta com estrelas
            itemsIndexed(questions) { index, question ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = "${index + 1}. $question",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    StarRating(
                        selectedStars = answers[index],
                        onStarsSelected = { selectedStars ->
                            answers[index] = selectedStars
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    )
                }
            }

            // Espaçamento final antes do botão
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Botão para enviar respostas fixado na parte inferior
        Button(
            onClick = {
                println("Respostas enviadas: $answers")
                navigateBack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C3483),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Enviar Respostas",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)

            )
        }
    }
}
