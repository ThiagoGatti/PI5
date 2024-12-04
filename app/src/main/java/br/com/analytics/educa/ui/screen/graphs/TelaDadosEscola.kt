package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.data.model.buscarDadosEscola
import br.com.analytics.educa.data.model.mediaEscola

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
            onError = { error -> errorMessage = error }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (errorMessage != null) {
                Text(
                    text = "Erro: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Spacer(modifier = Modifier.weight(0.5f)) // Espaço acima para empurrar as informações para baixo

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Ícone de Livro
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Ícone de Livro",
                        tint = Color.White,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Nome da Escola
                    Text(
                        text = "$nomeEscola",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Média Geral
                    Text(
                        text = "Média Geral: ${"%.1f".format(mediaNotas)}",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Médias Individuais com Ícones
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Ícone Aluno",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Média Aluno: ${"%.1f".format(mediasPorTipo["ALUNO"] ?: 0f)}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = "Ícone Funcionário",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Média Funcionário: ${"%.1f".format(mediasPorTipo["FUNCIONARIO"] ?: 0f)}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Ícone Professor",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Média Professor: ${"%.1f".format(mediasPorTipo["PROFESSOR"] ?: 0f)}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Ícone de Gráfico",
                    tint = Color.White,
                    modifier = Modifier
                        .size(230.dp)
                        .padding(vertical = 3.dp)
                )


                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botões de gráficos
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = navigateToTelaGraficoBarra,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D3DC8)),
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text("Gráficos", color = Color.White)
                        }

                        Button(
                            onClick = navigateToTelaGraficoPizza,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D3DC8)),
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text("Comparação", color = Color.White)
                        }
                    }

                    // Botão de Voltar
                    Button(
                        onClick = navigateBack,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                        modifier = Modifier
                            .width(160.dp)
                            .height(60.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Ícone de voltar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Voltar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
