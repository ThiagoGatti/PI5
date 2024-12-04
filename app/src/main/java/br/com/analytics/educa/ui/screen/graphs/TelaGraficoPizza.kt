package br.com.analytics.educa.ui.screen.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.buscarDadosEscola
import br.com.analytics.educa.ui.component.design.GraficoPizza

@Composable
fun TelaGraficoPizza(
    username: String,
    navigateBack: () -> Unit
) {
    var nomeEscola by remember { mutableStateOf("Carregando...") }
    var mediaNotas by remember { mutableStateOf(0f) }
    var dadosGrafico by remember { mutableStateOf<Map<String, Float>>(emptyMap()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        buscarDadosEscola(
            login = username,
            onResult = { nome, _, mediasPorTipo ->
                nomeEscola = nome
                dadosGrafico = mediasPorTipo
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))))
            .padding(16.dp)
    ) {
        if (errorMessage != null) {
            Text(
                text = "Erro: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Comparação de Usuários",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                if (dadosGrafico.isNotEmpty()) {
                    GraficoPizza(
                        dados = dadosGrafico,
                        titulo = "Distribuição por Tipo de Usuário"
                    )
                } else {
                    Text(
                        text = "Carregando gráfico...",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Button(
                    onClick = navigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9752E7)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text("Voltar", color = Color.White)
                }
            }
        }
    }
}