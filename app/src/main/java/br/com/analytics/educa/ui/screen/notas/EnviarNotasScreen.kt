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
import br.com.analytics.educa.ui.component.usecase.EnviarNotaUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnviarNotasScreen(
    login: String,
    navigateBack: () -> Unit,
) {
    val turmas = listOf("Turma 1", "Turma 2", "Turma 3")

    // Função fictícia para buscar os alunos por turma
    val getAlunosByTurma = { turma: String ->
        when (turma) {
            "Turma 1" -> listOf("Aluno 1", "Aluno 2", "Aluno 3")
            "Turma 2" -> listOf("Aluno 4", "Aluno 5", "Aluno 6")
            "Turma 3" -> listOf("Aluno 7", "Aluno 8", "Aluno 9")
            else -> emptyList()
        }
    }

    // Função fictícia de envio de notas e frequência
    val enviarNotaEFrequencia = { turma: String, aluno: String, nota: String, frequencia: String ->
        println("Enviando para $aluno da $turma: Nota = $nota, Frequência = $frequencia")
    }

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
                TurmasListNotas(
                    turmas = turmas,
                    onTurmaSelected = { turmaSelecionada = it },
                )
            } else if (alunoSelecionado == null) {
                AlunosList(
                    alunos = alunos,
                    onAlunoSelected = { alunoSelecionado = it },
                    onBackToTurmas = { turmaSelecionada = null }
                )
            } else {
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
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = faltas,
                        onValueChange = {
                            if (it.toIntOrNull() != null) {
                                faltas = it
                            }
                        },
                        label = { Text("Número de Faltas", color = Color.White) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            // Verifica se a nota e as faltas são válidas
                            if (nota.toDoubleOrNull() != null && faltas.toIntOrNull() != null) {
                                enviarNotaEFrequencia(
                                    turmaSelecionada!!,
                                    alunoSelecionado!!,
                                    nota,
                                    faltas
                                )
                                showSuccessMessage = true
                                showErrorMessage = false
                            } else {
                                showSuccessMessage = false
                                showErrorMessage = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Enviar Nota e Frequência")
                    }
                }
            }
        }
    }
}

// Funções auxiliares para listagem de turmas e alunos
@Composable
fun TurmasListNotas(turmas: List<String>, onTurmaSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        turmas.forEach { turma ->
            Button(
                onClick = { onTurmaSelected(turma) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = turma)
            }
        }
    }
}

@Composable
fun AlunosList(alunos: List<String>, onAlunoSelected: (String) -> Unit, onBackToTurmas: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onBackToTurmas() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Voltar para as Turmas")
        }

        alunos.forEach { aluno ->
            Button(
                onClick = { onAlunoSelected(aluno) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = aluno)
            }
        }
    }
}