package br.com.analytics.educa.ui.screen.notas

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
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


    LaunchedEffect(key1 = login) {
        fetchUserCompleto(login, onResult = { userCompleto ->
            if (userCompleto.type == "PROFESSOR" && userCompleto.components["turmas"] != null) {
                val turmasList = userCompleto.components["turmas"] as List<String>
                turmas = turmasList
            }
            materia = userCompleto.components["materia"] as String
        }, onError = { error -> showErrorMessage = true })
    }


    LaunchedEffect(key1 = turmaSelecionada) {
        turmaSelecionada?.let { turma ->
            fetchUsersByTurma(turma) { listaAlunos ->
                alunos = listaAlunos
            }
        }
    }

    val enviarNota = {
        if (nota.isNotBlank() && faltas.isNotBlank() && materia.isNotBlank() && alunoSelecionado != null) {
            val frequencia = faltas.toIntOrNull() ?: 0
            val notaDouble = nota.toDoubleOrNull() ?: 0.0
            enviarNotaPresenca(
                login = alunoSelecionado!!.login,
                materia = materia,
                nota = notaDouble,
                frequencia = validarFaltas(frequencia.toString())?.toInt() ?: 0,
                onResult = {
                    showSuccessMessage = true
                    showErrorMessage = false
                },
                onError = { error -> showErrorMessage = true; showSuccessMessage = false }
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Enviar Notas e Frequência",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (showSuccessMessage) {
                Text(
                    text = "Dados enviados com sucesso!",
                    color = Color.Green,
                    modifier = Modifier.padding(8.dp)
                )
            } else if (showErrorMessage) {
                Text(
                    text = "Erro ao enviar os dados. Verifique as informações.",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            when {
                turmaSelecionada == null -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(turmas) { turma ->
                            Button(
                                onClick = { turmaSelecionada = turma },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(70.dp),
                                contentPadding = PaddingValues()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = Color(0xFF5D145B),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = mostrarTurma(turma),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = navigateBack,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                            .width(200.dp)
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Voltar",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                alunoSelecionado == null -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(alunos) { aluno ->
                            Button(
                                onClick = { alunoSelecionado = aluno },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(70.dp),
                                contentPadding = PaddingValues()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = Color(0xFF5D145B),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = aluno.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { turmaSelecionada = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                            .width(200.dp)
                            .height(50.dp)
                    ) {
                        Text(text = "Voltar às Turmas", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Aluno: ${alunoSelecionado!!.name}",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(8.dp)
                        )

                        OutlinedTextField(
                            value = nota,
                            onValueChange = {
                                if (it.isEmpty() || it.toDoubleOrNull() == null || it.toDoubleOrNull()!! <= 10.0) {
                                    nota = it
                                }
                            },
                            label = { Text("Nota", color = Color.White) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                cursorColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White
                            ),
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = faltas,
                            onValueChange = {
                                if (it.isEmpty() || it.toIntOrNull() != null && it.toIntOrNull()!! <= 20) {
                                    faltas = it
                                }
                            },
                            label = { Text("Frequência (insira a quantidade de faltas - total de aulas = 20)", color = Color.White) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                cursorColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White
                            ),
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = enviarNota,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = "Enviar",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { alunoSelecionado = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = "Voltar aos Alunos",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
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