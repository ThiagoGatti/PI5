package br.com.analytics.educa.ui.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.ui.component.StarRating
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun Formulario(
    tipoUsuario: String,
    form: String,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    // Lista de perguntas
    val questions = retorna_perguntas(tipoUsuario, form)

    // Lembrar Respostas
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
                // Título
                Text(
                    text = "Formulário - $form",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Exibir perguntas com as estrelinhas
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

            // Botão para enviar respostas
            item {
                Spacer(modifier = Modifier.height(16.dp)) // Espaçamento antes do botão
                Button(
                    onClick = {
                        Toast.makeText(context, "Respostas enviadas.", Toast.LENGTH_LONG).show()
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
    }
}

fun retorna_perguntas(tipoUsuario: String, form: String): List<String> {
    return when (tipoUsuario) {
        "Aluno" -> when (form) {
            "Autonomia e Protagonismo" -> listOf(
                "Você se sente desafiado e motivado nas aulas?",
                "Você se sente à vontade para fazer perguntas e expressar suas dúvidas?",
                "Você questiona os conteúdos e as informações apresentadas em sala de aula?",
                "Você assume a responsabilidade pelo seu processo de aprendizagem?",
                "Você se esforça para compreender os conteúdos e se dedica às atividades propostas?",
                "Você busca novas informações e conhecimentos além do que é apresentado em sala de aula?",
                "Você participa ativamente das discussões e atividades em sala de aula?",
                "Você busca diferentes perspectivas e pontos de vista sobre os temas abordados?",
                "Você é capaz de pensar de forma crítica e criativa, propondo soluções e ideias inovadoras?",
                "Você se reconhece como agente de transformação social?"
            )

            "Clima Escolar" -> listOf(
                "Você se sente acolhido e respeitado na escola?",
                "Você se sente seguro na escola?",
                "Você tem amigos na escola?",
                "Você se relaciona de forma respeitosa e colaborativa com seus colegas?",
                "Você contribui para o bom andamento das aulas e para o aprendizado coletivo?",
                "Você se sente parte de uma comunidade de aprendizagem?"
            )

            "Qualidade do Ensino" -> listOf(
                "Você acha que o educador é qualificado e experiente?",
                "Você acha que o educador explica os conteúdos de forma clara e objetiva?",
                "Você acha que as aulas são interessantes e dinâmicas?",
                "Você acha que o educador se preocupa com o seu aprendizado?",
                "Você relaciona os conteúdos aprendidos com sua realidade e com o mundo ao seu redor?",
                "As aulas te incentivam a refletir sobre seu papel na sociedade e a buscar soluções para os problemas do mundo?",
                "Você tem feedback regular sobre seu desempenho?",
                "Você tem acesso aos recursos e materiais necessários para aprender?"
            )

            "Infraestrutura" -> listOf(
                "Você acha que a escola está bem equipada e conservada?",
                "Você acha que os banheiros e as salas de aula são limpos e higiênicos?",
                "Você acha que a biblioteca é bem organizada e possui um acervo adequado?",
                "Você acha que a quadra esportiva está em boas condições?"
            )

            "Gestão" -> listOf(
                "Você acha que a escola é bem organizada e administrada?",
                "Você acha que a comunicação entre a escola e os pais é eficaz?",
                "Você acha que a escola é transparente e democrática?",
                "Você se sente ouvido e valorizado na escola?"
            )

            "Considerações Finais" -> listOf(
                "Quais aspectos da sua participação nas aulas você considera mais positivos?",
                "Quais aspectos da sua participação nas aulas você considera que podem ser melhorados?",
                "Que sugestões você tem para o aprimoramento das aulas e do seu processo de aprendizagem?"
            )

            else -> listOf("Formulário não encontrado.")
        }

        "Professor" -> when (form) {
            "Condições de Trabalho" -> listOf(
                "Você se sente satisfeito com as condições de trabalho na escola?",
                "Você tem acesso aos recursos e materiais necessários para dar aulas?",
                "Você se sente apoiado pela equipe diretiva e pelos colegas?",
                "Você tem tempo para planejamento e desenvolvimento profissional?"
            )

            "Qualidade da Educação" -> listOf(
                "Você acha que os alunos estão aprendendo e se desenvolvendo de forma satisfatória?",
                "Você tem autonomia para utilizar diferentes metodologias de ensino?",
                "Você incentiva a participação ativa dos educandos em sala de aula?",
                "Você promove o diálogo e a interação entre os educandos e com você?",
                "Você considera as diferentes visões e experiências dos alunos em suas aulas?",
                "Você considera o contexto social e cultural dos alunos em suas aulas?",
                "Você trabalha com temas relevantes para a realidade dos alunos?",
                "Você promove a inclusão e a valorização da diversidade em sala de aula?",
                "Você incentiva a autonomia e o protagonismo dos educandos em suas aulas?",
                "Você oferece aos alunos oportunidades para tomar decisões e serem protagonistas do processo de aprendizagem?",
                "Você utiliza métodos e estratégias de ensino que estimulam a criticidade e a criatividade dos educandos?",
                "Você promove a interdisciplinaridade e a conexão entre diferentes áreas do conhecimento?",
                "Você utiliza diferentes instrumentos de avaliação para acompanhar o processo de aprendizagem dos educandos?",
                "Você considera a progressão individual de cada aluno na avaliação da aprendizagem?",
                "Você se sente desafiado e motivado em sua prática docente?"
            )

            "Clima Escolar" -> listOf(
                "Você se sente acolhido e respeitado na escola?",
                "Você se sente seguro na escola?",
                "Você tem um bom relacionamento com seus colegas e com a equipe diretiva?",
                "Você se sente parte de uma comunidade de aprendizagem?"
            )

            "Participação" -> listOf(
                "Você participa de atividades de formação continuada?",
                "Você participa de decisões importantes da escola?",
                "Você se sente valorizado e reconhecido por seu trabalho?",
                "Você tem oportunidades de crescimento profissional?"
            )

            "Desafios" -> listOf(
                "Quais são os principais desafios que você enfrenta em sua prática docente?",
                "Quais são as suas sugestões para melhorar a qualidade da educação na escola?",
                "Quais são as suas expectativas para o futuro da escola?"
            )

            else -> listOf("Formulário não encontrado.")
        }

        "Funcionario" -> when (form) {
            "Satisfação no Trabalho" -> listOf(
                "Você se sente satisfeito com o seu trabalho na escola?",
                "Você se sente valorizado e reconhecido por seu trabalho?",
                "Você tem boas condições de trabalho?",
                "Você tem um bom relacionamento com seus colegas e com a equipe diretiva?"
            )

            "Eficiência da Gestão" -> listOf(
                "Você acha que a escola é bem organizada e administrada?",
                "Você acha que a comunicação interna da escola é eficaz?",
                "Você acha que a escola é transparente e democrática?",
                "Você se sente ouvido e valorizado na escola?"
            )

            "Infraestrutura" -> listOf(
                "Você acha que a escola está bem equipada e conservada?",
                "Você acha que os banheiros e as áreas de trabalho são limpos e higiênicos?",
                "Você tem acesso aos recursos e materiais necessários para realizar seu trabalho?",
                "Você se sente seguro no ambiente de trabalho?"
            )

            "Desafios" -> listOf(
                "Quais são os principais desafios que você enfrenta em seu trabalho?",
                "Quais são as suas sugestões para melhorar a qualidade da educação na escola?",
                "Quais são as suas expectativas para o futuro da escola?"
            )

            else -> listOf("Formulário não encontrado.")
        }

        else -> listOf("Tipo de usuário não encontrado.")
    }
}
