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
import br.com.analytics.educa.data.model.agruparRespostasPorTipoPessoa
import br.com.analytics.educa.data.model.buscarRespostasPorEscola
import br.com.analytics.educa.data.retrofit.ResponseBySchool
import br.com.analytics.educa.ui.component.GraficoBarraRespostasPorFormulario

@Composable
fun TelaGraficos(
    username: String,
    navigateBack: () -> Unit
) {
    var respostasPorEscola by remember { mutableStateOf<List<ResponseBySchool>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var formularioSelecionado by remember { mutableStateOf("") }
    var formulariosDisponiveis by remember { mutableStateOf(emptyList<String>()) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Busca dados ao iniciar
    LaunchedEffect(Unit) {
        buscarRespostasPorEscola(
            login = username,
            onResult = { respostas ->
                respostasPorEscola = respostas
                formulariosDisponiveis = respostas.map { it.nome_formulario }.distinct()
                if (formulariosDisponiveis.isNotEmpty()) {
                    formularioSelecionado = formulariosDisponiveis.first()
                }
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }

    // Agrupando os dados
    val agrupamento = if (respostasPorEscola.isNotEmpty()) {
        respostasPorEscola
            .groupBy { it.nome_formulario }
            .mapValues { (_, respostas) ->
                respostas.flatMap { it.respostas.entries }
                    .groupBy({ it.key }, { it.value.toFloat() })
                    .mapValues { (_, valores) -> valores.average().toFloat() }
            }
    } else {
        emptyMap()
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
            } else if (agrupamento.isNotEmpty() && formulariosDisponiveis.isNotEmpty()) {
                // Lista suspensa para selecionar o formulário
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    OutlinedButton(
                        onClick = { dropdownExpanded = true },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF9752E7))
                    ) {
                        Text(text = formularioSelecionado.ifEmpty { "Selecione um formulário" }, color = Color.White)
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        formulariosDisponiveis.forEach { formulario ->
                            DropdownMenuItem(
                                text = { Text(formulario, color = Color.Black) },
                                onClick = {
                                    formularioSelecionado = formulario
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                if (formularioSelecionado.isNotEmpty() && agrupamento.containsKey(formularioSelecionado)) {
                    GraficoBarraRespostasPorFormulario(
                        agrupamento = agrupamento,
                        formularioSelecionado = formularioSelecionado
                    )
                }
            } else {
                Text(
                    text = "Carregando dados...",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Button(
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9752E7)),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text("Voltar", color = Color.White)
            }
        }
    }
}