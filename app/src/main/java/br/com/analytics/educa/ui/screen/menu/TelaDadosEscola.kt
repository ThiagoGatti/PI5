package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.buscarDadosEscola
import br.com.analytics.educa.data.model.mediaEscola
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun TelaDadosEscola(
    login: String,
    navigateToTelaGraficoBarra: () -> Unit,
    navigateToTelaGraficoPizza: () -> Unit,
    navigateBack: () -> Unit
) {
    var nomeEscola by remember { mutableStateOf("Carregando...") }
    var mediaNotas by remember { mutableStateOf(0f) }
    var mediasPorTipo by remember { mutableStateOf<Map<String, Float>>(emptyMap()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        buscarDadosEscola(
            login = login,
            onResult = { nome, media, medias ->
                nomeEscola = nome
                mediaNotas = media
                mediasPorTipo = medias
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "$nomeEscola: ${mediaEscola(mediasPorTipo, mediaNotas)}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Média Geral: ${"%.1f".format(mediaNotas)}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                mediasPorTipo.forEach { (tipo, media) ->
                    Text(
                        text = "Média $tipo: ${"%.1f".format(media)}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Button(
                    onClick = navigateToTelaGraficoBarra,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9752E7)),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Ir para Gráfico de Barra", color = Color.White)
                }
                Button(
                    onClick = navigateToTelaGraficoPizza,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9752E7)),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Ir para Gráfico de Pizza", color = Color.White)
                }
                Button(
                    onClick = navigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9752E7)),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Voltar", color = Color.White)
                }
            }
        }
    }
}