package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.ui.component.StarRating

@Composable
fun AnswerFormAlunoAutonomia(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF8E44AD), Color(0xFFD2B4DE))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Título estilizado
        Text(
            text = "Formulário - Autonomia e Protagonismo",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Perguntas e seleção de estrelas
        questions.forEachIndexed { index, question ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "${index + 1}. $question",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                StarRating(
                    selectedStars = answers[index],
                    onStarsSelected = { selectedStars ->
                        answers[index] = selectedStars // Atualiza a resposta ao selecionar
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Botão para enviar respostas
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
                .padding(top = 16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Enviar Respostas",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
