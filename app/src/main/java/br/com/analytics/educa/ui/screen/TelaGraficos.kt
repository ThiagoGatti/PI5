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
import br.com.analytics.educa.data.model.buscarRespostasPorEscola
import br.com.analytics.educa.data.retrofit.ResponseBySchool
import br.com.analytics.educa.ui.component.GraficoBarraMediaFormularios
import android.content.res.Configuration
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun TelaGraficos(
    username: String,
    userType: String,
    navigateBack: () -> Unit
) {
    // Estado principal para reloading da tela
    var filtroTipoUsuario by remember { mutableStateOf(userType) }
    var reloadKey by remember { mutableStateOf(0) } // Chave para forçar recomposição

    // Recarrega a tela quando `reloadKey` muda
    LaunchedEffect(reloadKey) {
        // Não faz nada diretamente, só força a recomposição da tela
    }

    TelaGraficosInterna(
        username = username,
        tipoSelecionado = filtroTipoUsuario,
        onTipoUsuarioChange = { novoTipo ->
            filtroTipoUsuario = novoTipo
            reloadKey++ // Força a recomposição
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

    // Busca dados ao iniciar
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

    // Detecta a orientação da tela
    val configuration = LocalConfiguration.current
    val alignment = remember(configuration.orientation) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Alignment.TopEnd
        } else {
            Alignment.BottomCenter
        }
    }
    val paddingValues = remember(configuration.orientation) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Modifier.padding(16.dp)
        } else {
            Modifier.padding(16.dp, 0.dp, 16.dp, 200.dp)
        }
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gráficos de Respostas",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = "Erro: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (isLoading) {
                Text(
                    text = "Carregando dados...",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (respostasPorEscola.isNotEmpty()) {
                // Lista suspensa para selecionar o tipo de usuário
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    OutlinedButton(
                        onClick = { dropdownExpanded = !dropdownExpanded },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF9752E7))
                    ) {
                        Text(
                            text = tipoSelecionado.ifEmpty { "Selecione um tipo de usuário" },
                            color = Color.White
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
                                    onTipoUsuarioChange(tipo) // Notifica o novo tipo
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                if (agrupamentoMedias.isNotEmpty()) {
                    GraficoBarraMediaFormularios(
                        medias = agrupamentoMedias
                    )
                } else {
                    Text(
                        text = "Nenhum dado disponível para este tipo de usuário.",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Button(
            onClick = navigateBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9752E7)),
            modifier = paddingValues.align(alignment)
        ) {
            Text("Voltar", color = Color.White)
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