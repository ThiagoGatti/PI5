package br.com.analytics.educa.ui.screen.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.buscarRespostasPorEscola
import br.com.analytics.educa.data.retrofit.ResponseBySchool
import br.com.analytics.educa.ui.component.design.GraficoBarraMediaFormularios
import androidx.compose.ui.Modifier

@Composable
fun TelaGraficoBarra(
    username: String,
    userType: String,
    navigateBack: () -> Unit
) {
    var filtroTipoUsuario by remember { mutableStateOf(if (userType == "DIRETOR") "ALUNO" else userType) }
    var reloadKey by remember { mutableStateOf(0) }

    LaunchedEffect(reloadKey) {}

    TelaGraficosInterna(
        username = username,
        tipoSelecionado = filtroTipoUsuario,
        onTipoUsuarioChange = { novoTipo ->
            filtroTipoUsuario = novoTipo
            reloadKey++
        },
        navigateBack = navigateBack
    )
}

@Composable
private fun TelaGraficosInterna(
    username: String,
    tipoSelecionado: String,
    onTipoUsuarioChange: (String) -> Unit,
    navigateBack: () -> Unit
) {
    var respostasPorEscola by remember { mutableStateOf<List<ResponseBySchool>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var tiposDeUsuario by remember { mutableStateOf(listOf(tipoSelecionado)) }
    var agrupamentoMedias by remember { mutableStateOf<Map<String, Float>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(tipoSelecionado) {
        isLoading = true
        buscarRespostasPorEscola(
            login = username,
            onResult = { respostas ->
                respostasPorEscola = respostas
                tiposDeUsuario = respostas.map { it.tipo_usuario }.distinct()
                atualizarMedias(respostas, tipoSelecionado) { medias ->
                    agrupamentoMedias = medias
                }
                isLoading = false
            },
            onError = { error ->
                errorMessage = error
                isLoading = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Gráficos de Respostas",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = "Erro: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else if (isLoading) {
                Text(
                    text = "Carregando dados...",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else if (respostasPorEscola.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    OutlinedButton(
                        onClick = { dropdownExpanded = !dropdownExpanded },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF9752E7)),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = tipoSelecionado.ifEmpty { "Selecione um tipo de usuário" },
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        tiposDeUsuario.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo, color = Color.Black) },
                                onClick = {
                                    onTipoUsuarioChange(tipo)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                if (agrupamentoMedias.isNotEmpty()) {
                    GraficoBarraMediaFormularios(medias = agrupamentoMedias)
                } else {
                    Text(
                        text = "Nenhum dado disponível para este tipo de usuário.",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }

        Button(
            onClick = navigateBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(0.5f)
                .height(50.dp)
        ) {
            Text(
                text = "Voltar",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun atualizarMedias(
    respostas: List<ResponseBySchool>,
    tipoSelecionado: String,
    onResult: (Map<String, Float>) -> Unit
) {
    val medias = respostas
        .filter { it.tipo_usuario == tipoSelecionado }
        .groupBy { it.nome_formulario }
        .mapValues { (_, respostas) ->
            respostas.flatMap { it.respostas.values }
                .average()
                .toFloat()
        }
    onResult(medias)
}
