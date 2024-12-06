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
import br.com.analytics.educa.data.model.enviarNotaPresenca
import br.com.analytics.educa.data.model.fetchUserCompleto
import br.com.analytics.educa.data.model.fetchUsersByTurma
import br.com.analytics.educa.data.model.mostrarTurma
import br.com.analytics.educa.data.retrofit.User
import br.com.analytics.educa.ui.component.usecase.validarFaltas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnviarNotasScreen(
    login: String,
    navigateBack: () -> Unit,
) {
    var turmas by remember { mutableStateOf<List<String>>(emptyList()) }
    var alunos by remember { mutableStateOf<List<User>>(emptyList()) }
    var turmaSelecionada by remember { mutableStateOf<String?>(null) }
    var alunoSelecionado by remember { mutableStateOf<User?>(null) }
    var nota by remember { mutableStateOf("") }
    var faltas by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var materia by remember { mutableStateOf("") }

    // Função para buscar as turmas do professor
    LaunchedEffect(key1 = login) {
        fetchUserCompleto(login, onResult = { userCompleto ->
            if (userCompleto.type == "PROFESSOR" && userCompleto.components["turmas"] != null) {
                // O professor leciona em várias turmas
                val turmasList = userCompleto.components["turmas"] as List<String>
                turmas = turmasList
            }
            materia = userCompleto.components["materia"] as String
        }, onError = { error ->
            showErrorMessage = true
        })
    }

    // Função para buscar os alunos de uma turma
    val getAlunosByTurma = { turma: String ->
        fetchUsersByTurma(turma) { listaAlunos ->
            alunos = listaAlunos
        }
    }

    // Função para enviar nota e frequência
    val enviarNota = {
        if (nota.isNotBlank() && faltas.isNotBlank() && materia.isNotBlank() && alunoSelecionado != null) {
            val frequencia = faltas.toIntOrNull() ?: 0
            val notaDouble = nota.toDoubleOrNull() ?: 0.0
            println("Boletim a ser enviado: {${alunoSelecionado!!.login}, $materia, $notaDouble, $frequencia}")
            enviarNotaPresenca(
                login = alunoSelecionado!!.login, // Login do aluno selecionado
                materia = materia,
                nota = notaDouble,
                frequencia = validarFaltas(frequencia.toString())?.toInt() ?: 0,
                onResult = {
                    showSuccessMessage = true
                    showErrorMessage = false
                },
                onError = { error ->
                    showErrorMessage = true
                    showSuccessMessage = false
                }
            )
        }
    }

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
                    onTurmaSelected = { turmaSelecionada = it; getAlunosByTurma(it) },
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
                        text = "Aluno: ${alunoSelecionado!!.name}",
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
                        label = { Text("Nota") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = faltas,
                        onValueChange = {
                            if (it.toIntOrNull() != null) {
                                faltas = it
                            }
                        },
                        label = { Text("Frequência (faltas)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { enviarNota() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar Nota e Frequência")
                    }
                }
            }
        }
    }
}

@Composable
fun AlunosList(
    alunos: List<User>,
    onAlunoSelected: (User) -> Unit,
    onBackToTurmas: () -> Unit
) {
    Column {
        Button(
            onClick = onBackToTurmas,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text = "Voltar às Turmas")
        }

        alunos.forEach { aluno ->
            Button(
                onClick = { onAlunoSelected(aluno) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = aluno.name)
            }
        }
    }
}

@Composable
fun TurmasListNotas(
    turmas: List<String>,
    onTurmaSelected: (String) -> Unit
) {
    Column {
        turmas.forEach { turma ->
            Button(
                onClick = { onTurmaSelected(turma) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = mostrarTurma(turma))
            }
        }
    }
}