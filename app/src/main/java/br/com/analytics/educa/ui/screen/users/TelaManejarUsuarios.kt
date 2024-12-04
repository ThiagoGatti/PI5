package br.com.analytics.educa.ui.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.fetchTurmas
import br.com.analytics.educa.data.model.fetchUsersByTurma
import br.com.analytics.educa.data.model.fetchUsersByType
import br.com.analytics.educa.data.model.removeUser
import br.com.analytics.educa.data.retrofit.User
import kotlinx.coroutines.launch

@Composable
fun TelaManejarUsuarios(
    currentUserLogin: String,
    navigateBack: () -> Unit
) {
    var selectedUserType by remember { mutableStateOf<String?>(null) }
    var selectedTurma by remember { mutableStateOf<String?>(null) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var usersList by remember { mutableStateOf<List<User>>(emptyList()) }
    var turmasList by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

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
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Gerenciar Usuários",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            when {
                selectedUserType == null -> {
                    // Seleção de tipo de usuário
                    Text(
                        text = "Selecione o tipo de usuário:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    listOf("ALUNO", "PROFESSOR", "FUNCIONARIO", "DIRETOR").forEach { tipo ->
                        Button(
                            onClick = {
                                selectedUserType = tipo
                                if (tipo == "ALUNO") {
                                    fetchTurmas { turmas ->
                                        turmasList = turmas
                                    }
                                } else {
                                    fetchUsersByType(tipo) { users ->
                                        usersList = users
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(tipo, color = Color.White)
                        }
                    }
                }

                selectedUserType == "ALUNO" && selectedTurma == null -> {
                    // Exibição de turmas
                    Text(
                        text = "Selecione a turma:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    turmasList.forEach { turma ->
                        Button(
                            onClick = {
                                selectedTurma = turma
                                fetchUsersByTurma(
                                    turma = turma,
                                    onResult = { users ->
                                        usersList = users
                                    },
                                    onError = { errorMessage ->
                                        println("Erro ao buscar usuários: $errorMessage")
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text("${turma.first()}° Ano ${turma.last()}", color = Color.White)
                        }
                    }
                    Button(
                        onClick = { selectedUserType = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Voltar", color = Color.White)
                    }
                }

                else -> {
                    // Exibição de lista de usuários
                    usersList.forEach { user ->
                        val isCurrentUser = user.login == currentUserLogin
                        Button(
                            onClick = { if (!isCurrentUser) selectedUser = user },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = if (isCurrentUser) ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            ) else ButtonDefaults.buttonColors()
                        ) {
                            Text(
                                text = "${user.name} (${user.login})",
                                color = if (isCurrentUser) Color.Black else Color.White
                            )
                        }
                    }

                    // Botão Voltar após a lista de usuários
                    Button(
                        onClick = {
                            if (selectedUserType == "ALUNO") selectedTurma =
                                null else selectedUserType = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Voltar", color = Color.White)
                    }
                }
            }

            if (selectedUser != null) {
                AlertDialog(
                    onDismissRequest = { selectedUser = null },
                    title = { Text("Ações para ${selectedUser?.name}") },
                    text = { Text("Escolha uma ação:") },
                    confirmButton = {
                        Column {
                            Button(
                                onClick = {
                                    showEditDialog = true
                                    selectedUser = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Editar")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    showRemoveDialog = true
                                    selectedUser = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Remover")
                            }
                        }
                    },
                    dismissButton = {
                        Button(onClick = { selectedUser = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showEditDialog) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text("Editar Usuário") },
                    text = {
                        Column {
                            Text("Implementar formulário de edição para: ${selectedUser?.name}")
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showEditDialog = false }) {
                            Text("Salvar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showEditDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showRemoveDialog) {
                AlertDialog(
                    onDismissRequest = { showRemoveDialog = false },
                    title = { Text("Remover Usuário") },
                    text = { Text("Deseja realmente remover ${selectedUser?.name}?") },
                    confirmButton = {
                        Button(onClick = {
                            scope.launch {
                                selectedUser?.let { user ->
                                    removeUser(user.login) {
                                        usersList = usersList.filter { it.login != user.login }
                                        showRemoveDialog = false
                                    }
                                }
                            }
                        }) {
                            Text("Remover")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showRemoveDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}