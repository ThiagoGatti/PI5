package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.agruparRespostasPorTipoPessoa
import br.com.analytics.educa.data.model.buscarRespostasPorEscola
import br.com.analytics.educa.data.retrofit.ResponseBySchool
import br.com.analytics.educa.ui.component.GraficoPizza

@Composable
fun TelaGraficos(
    username: String,
    navigateBack: () -> Unit
) {
    var respostasPorEscola by remember { mutableStateOf<List<ResponseBySchool>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        buscarRespostasPorEscola(
            login = username,
            onResult = { respostas ->
                respostasPorEscola = respostas
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }

    // Agrupando os dados
    val agrupamento = if (respostasPorEscola.isNotEmpty()) {
        agruparRespostasPorTipoPessoa(respostasPorEscola)
    } else {
        emptyMap()
    }

    // Calculando as médias gerais
    val mediasPorTipoPessoa = calcularMediaGeralPorTipoPessoa(agrupamento)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (errorMessage != null) {
            Text(
                text = "Erro: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        } else if (agrupamento.isNotEmpty()) {
            // Exibindo o gráfico de pizza
            GraficoPizza(
                dados = mediasPorTipoPessoa.map { it.key to it.value }.toMap(),
                titulo = "Média Geral por Tipo de Pessoa"
            )
        } else {
            Text(
                text = "Carregando dados...",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
        }

        Button(
            onClick = navigateBack,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Voltar")
        }
    }
}

fun calcularMediaGeralPorTipoPessoa(agrupamento: Map<String, Map<String, Map<String, Float>>>): Map<String, Float> {
    val mediasPorTipoPessoa = mutableMapOf<String, Float>()

    agrupamento.forEach { (tipoPessoa, formularios) ->
        val totalNotas = formularios.values.flatMap { it.values }.sum()
        val totalPerguntas = formularios.values.flatMap { it.values }.size
        val media = if (totalPerguntas > 0) totalNotas / totalPerguntas else 0f

        mediasPorTipoPessoa[tipoPessoa] = media
    }

    return mediasPorTipoPessoa
}