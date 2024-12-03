package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.agruparRespostas
import br.com.analytics.educa.data.model.buscarRespostasPorEscola
import br.com.analytics.educa.data.retrofit.ResponseBySchool
import br.com.analytics.educa.ui.component.GraficoBarraRespostas
import br.com.analytics.educa.ui.component.GraficoLinhaTendencias

@Composable
fun TelaGraficos(
    username: String,
    navigateBack: () -> Unit
) {
    // Estado para armazenar respostas por escola
    var respostasPorEscola by remember { mutableStateOf<List<ResponseBySchool>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Busca as respostas do banco de dados ao montar a tela
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

    // Agrupa as respostas para os gráficos
    val agrupamento = if (respostasPorEscola.isNotEmpty()) {
        agruparRespostas(respostasPorEscola)
    } else {
        emptyMap()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Gráficos de Respostas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = "Erro: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (agrupamento.isNotEmpty()) {
                GraficoBarraRespostas(agrupamento)
                GraficoLinhaTendencias(agrupamento, pergunta = "q1") // Substituir "q1" pela pergunta desejada
            } else {
                Text(
                    text = "Carregando dados...",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
            }
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