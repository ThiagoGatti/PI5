package br.com.analytics.educa.ui.screen

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.agruparRespostas
import br.com.analytics.educa.data.model.buscarRespostasPorEscola
import br.com.analytics.educa.data.retrofit.ResponseBySchool
import br.com.analytics.educa.ui.component.GraficoBarraRespostas
import br.com.analytics.educa.ui.component.GraficoLinhaTendencias

@Composable
fun TelaGraficos(login: String, navigateBack: () -> Unit) {
    val context = LocalContext.current
    var respostasPorEscola by remember { mutableStateOf<List<ResponseBySchool>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        buscarRespostasPorEscola(
            login = login,
            onResult = { respostas ->
                respostasPorEscola = respostas
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }

    val agrupamento = if (respostasPorEscola.isNotEmpty()) {
        agruparRespostas(respostasPorEscola)
    } else {
        emptyMap()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(brush = Brush.horizontalGradient(colors = listOf(Color(0xFF00FF00), Color(0xFFFF0000))))
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
                text = "Gráficos por Escola",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            if (errorMessage != null) {
                /*Text(
                    text = "Erro: $errorMessage",
                    color = Color.RED,
                    modifier = Modifier.padding(16.dp)
                )*/
            } else if (agrupamento.isNotEmpty()) {
                GraficoBarraRespostas(agrupamento)
                GraficoLinhaTendencias(agrupamento, pergunta = "q1")
            } else {
                /*Text(
                    text = "Carregando dados...",
                    color = Color.GRAY,
                    modifier = Modifier.padding(16.dp)
                )*/
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
