package br.com.analytics.educa.ui.screen.notas

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.foundation.background
import br.com.analytics.educa.domain.usecase.EnviarNotaUseCase
import br.com.analytics.educa.ui.component.lists.TurmasList
import br.com.analytics.educa.ui.component.lists.AlunosList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnviarNotasScreen(
    navigateBack: () -> Unit,
    turmas: List<String>,
    getAlunosByTurma: (String) -> List<String>,
    enviarNotaEFrequencia: (String, String, Double, Int) -> Unit
) {
    val enviarNotaUseCase = EnviarNotaUseCase()

    var turmaSelecionada by remember { mutableStateOf<String?>(null) }
    var alunoSelecionado by remember { mutableStateOf<String?>(null) }
    var nota by remember { mutableStateOf("") }
    var faltas by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    val alunos = turmaSelecionada?.let { getAlunosByTurma(it) } ?: emptyList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Título
            Text(
                text = "Enviar Notas e Frequência",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )

            // Mensagem de sucesso ou erro
            if (showSuccessMessage) {
                Text(
                    text = "Dados enviados com sucesso!",
                    color = Color.Green,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
            } else if (showErrorMessage) {
                Text(
                    text = "Erro ao enviar os dados. Verifique as informações.",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
            }

            // Listar Turmas
            if (turmaSelecionada == null) {
                TurmasList(
                    turmas = turmas,
                    onTurmaSelected = { turmaSelecionada = it }
                )
            } else if (alunoSelecionado == null) {
                // Listar Alunos
                AlunosList(
                    alunos = alunos,
                    onAlunoSelected = { alunoSelecionado = it },
                    onBackToTurmas = { turmaSelecionada = null }
                )
            } else {
                // Formulário para Envio de Nota e Faltas
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Aluno: $alunoSelecionado",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nota,
                        onValueChange = {
                            if (it.toDoubleOrNull() != null && it.count { c -> c == '.' } <= 1) {
                                nota = it
                            }
                        },
                        label = { Text("Nota (0.0 a 10.0)", color = Color.White) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    OutlinedTextField(
                        value = faltas,
                        onValueChange = {
                            if (it.toIntOrNull() != null) {
                                faltas = it
                            }
                        },
                        label = { Text("Faltas", color = Color.White) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    Button(
                        onClick = {
                            val notaValidada = enviarNotaUseCase.validarNota(nota)
                            val faltasValidadas = enviarNotaUseCase.validarFaltas(faltas)

                            if (notaValidada != null && faltasValidadas != null) {
                                enviarNotaEFrequencia(
                                    turmaSelecionada!!,
                                    alunoSelecionado!!,
                                    notaValidada,
                                    faltasValidadas
                                )
                                alunoSelecionado = null
                                nota = ""
                                faltas = ""
                                showSuccessMessage = true
                                showErrorMessage = false
                            } else {
                                showSuccessMessage = false
                                showErrorMessage = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Enviar")
                    }

                    Button(
                        onClick = { alunoSelecionado = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Voltar para Seleção de Aluno")
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = navigateBack,
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
            ) {
                Text("Voltar", color = Color.White)
            }
        }
    }
}


