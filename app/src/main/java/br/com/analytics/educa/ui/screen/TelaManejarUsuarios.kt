package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.ui.component.AlunosList
import br.com.analytics.educa.ui.component.CpfTextField
import br.com.analytics.educa.ui.component.TurmasList
import br.com.analytics.educa.ui.component.formatPhoneNumberPreservingCursor
import formatAndValidateDatePreservingCursor
import isValidDate
import isValidDateInput
import java.text.SimpleDateFormat
import java.util.*




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaManejarUsuarios(
    navigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showAlunoList by remember { mutableStateOf(false) }
    var selectedTurma by remember { mutableStateOf<String?>(null) }
    var alunosByTurma by remember { mutableStateOf<List<String>>(emptyList()) }
    var nome by remember { mutableStateOf("") }
    var dataNascimentoCursor by remember { mutableStateOf(0) }
    var cpfState by remember { mutableStateOf(TextFieldValue("")) }
    var dataNascimento by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var telefoneCursor by remember { mutableStateOf(0) }
    var login by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var expandedTipo by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val tipoOptions = listOf("ALUNO", "PROFESSOR", "FUNCIONARIO", "DIRETOR")
    val turmas = listOf("Turma 1", "Turma 2", "Turma 3")
    val alunosMockados = mapOf(
        "Turma 1" to listOf("Aluno 1", "Aluno 2", "Aluno 3"),
        "Turma 2" to listOf("Aluno 4", "Aluno 5", "Aluno 6"),
        "Turma 3" to listOf("Aluno 7", "Aluno 8", "Aluno 9")
    )

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gerenciar Usuários",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                IconButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Usuário",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (!showAlunoList) {
                // Lista de turmas
                TurmasList(
                    turmas = turmas,
                    onTurmaSelected = { turma ->
                        selectedTurma = turma
                        alunosByTurma = alunosMockados[turma].orEmpty()
                        showAlunoList = true
                    }
                )
            } else {
                // Lista de alunos
                AlunosList(
                    alunos = alunosByTurma,
                    onAlunoSelected = { aluno ->
                        println("Aluno selecionado: $aluno")
                    },
                    onBackToTurmas = {
                        showAlunoList = false
                    }
                )
            }
        }

        // Botão "Voltar" no rodapé
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Voltar",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }

    // Diálogo para cadastrar novo usuário
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Cadastrar Novo Usuário") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CpfTextField(
                        value = cpfState,
                        onValueChange = { cpfState = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = TextFieldValue(dataNascimento, TextRange(dataNascimentoCursor)),
                        onValueChange = { input ->
                            val result = formatAndValidateDatePreservingCursor(input.text, input.selection.start)
                            if (result != null) {
                                dataNascimento = result.first
                                dataNascimentoCursor = result.second
                            }
                        },
                        label = { Text("Data de Nascimento (DD/MM/YYYY)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = TextFieldValue(telefone, TextRange(telefoneCursor)),
                        onValueChange = { input ->
                            val (formatted, cursor) = formatPhoneNumberPreservingCursor(input.text, input.selection.start)
                            telefone = formatted
                            telefoneCursor = cursor
                        },
                        label = { Text("Telefone (Ex.: (XX) XXXXX-XXXX)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it },
                        label = { Text("Login") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = { Text("Senha") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = tipo,
                            onValueChange = {},
                            label = { Text("Tipo") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { expandedTipo = !expandedTipo }) {
                                    Icon(
                                        imageVector = if (expandedTipo) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = expandedTipo,
                            onDismissRequest = { expandedTipo = false }
                        ) {
                            tipoOptions.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        tipo = option
                                        expandedTipo = false
                                    },
                                    text = { Text(option) }
                                )
                            }
                        }
                    }

                    // Exibir mensagem de erro, se houver
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nome.isEmpty() || !isValidDate(dataNascimento) || telefone.isEmpty() || login.isEmpty() || senha.isEmpty() || tipo.isEmpty() || cpfState.text.length != 14) {
                            errorMessage = "Preencha todos os campos corretamente!"
                        } else {
                            println("Usuário cadastrado: Nome=$nome, CPF=${cpfState.text}, Data Nascimento=$dataNascimento, Telefone=$telefone, Login=$login, Tipo=$tipo")
                            errorMessage = null

                            // Limpar os campos após salvar
                            nome = ""
                            cpfState = TextFieldValue("")
                            dataNascimento = ""
                            telefone = ""
                            telefoneCursor = 0
                            login = ""
                            senha = ""
                            tipo = ""
                            dataNascimentoCursor = 0

                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                Button(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}




