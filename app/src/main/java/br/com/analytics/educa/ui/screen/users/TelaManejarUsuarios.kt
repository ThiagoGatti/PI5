package br.com.analytics.educa.ui.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.fetchTurmas
import br.com.analytics.educa.data.model.fetchUserCompleto
import br.com.analytics.educa.data.model.fetchUsersByTurma
import br.com.analytics.educa.data.model.fetchUsersByType
import br.com.analytics.educa.data.model.removeUser
import br.com.analytics.educa.data.model.updateUserCompleto
import br.com.analytics.educa.data.retrofit.User
import br.com.analytics.educa.data.retrofit.UserCompleto
import br.com.analytics.educa.ui.component.ActionDialog
import br.com.analytics.educa.ui.component.EditUserDialog
import br.com.analytics.educa.ui.component.RemoveUserDialog
import kotlinx.coroutines.launch

@Composable
fun TelaManejarUsuarios(
    currentUserLogin: String,
    navigateBack: () -> Unit
) {
    var selectedUserType by remember { mutableStateOf<String?>(null) }
    var selectedTurma by remember { mutableStateOf<String?>(null) }
    var selectedUser by remember { mutableStateOf<UserCompleto?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var usersList by remember { mutableStateOf<List<User>>(emptyList()) }
    var turmasList by remember { mutableStateOf<List<String>>(emptyList()) }

    val scope = rememberCoroutineScope()

    // Função para carregar usuários por tipo
    fun loadUsersByType(type: String) {
        fetchUsersByType(type) { users ->
            usersList = users
        }
    }

    // Função para carregar turmas
    fun loadTurmas() {
        fetchTurmas { turmas ->
            turmasList = turmas
        }
    }

    // Função para carregar usuários de uma turma
    fun loadUsersByTurma(turma: String) {
        fetchUsersByTurma(turma) { users ->
            usersList = users
        }
    }

    LaunchedEffect(Unit) {
        loadTurmas() // Carrega as turmas ao iniciar
    }

    // Layout principal
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
            Text("Gerenciar Usuários", style = MaterialTheme.typography.headlineMedium, color = Color.White)

            // Seleção de Tipo
            Text("Selecione o Tipo:", color = Color.White)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Aluno", "Professor", "Funcionário").forEach { type ->
                    Button(onClick = {
                        selectedUserType = type
                        loadUsersByType(type) // Carrega usuários por tipo
                    }) {
                        Text(type)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seleção de Turma (se o tipo for "Aluno")
            if (selectedUserType == "Aluno") {
                Text("Selecione a Turma:", color = Color.White)
                turmasList.forEach { turma ->
                    Button(onClick = {
                        selectedTurma = turma
                        loadUsersByTurma(turma) // Carrega usuários por turma
                    }) {
                        Text(turma)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Usuários
            Text("Usuários:", color = Color.White)
            usersList.forEach { user ->
                Button(
                    onClick = {
                        if (user.login != currentUserLogin) {
                            scope.launch {
                                fetchUserCompleto(user.login, { fetchedUser ->
                                    selectedUser = fetchedUser
                                    showEditDialog = true
                                }, { error ->
                                    println("Erro ao buscar usuário completo: $error")
                                })
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${user.name} (${user.login})")
                }
            }
        }

        // Diálogo para edição
        if (showEditDialog && selectedUser != null) {
            EditUserDialog(
                user = selectedUser!!,
                onDismiss = { showEditDialog = false },
                onSave = { updatedUser ->
                    scope.launch {
                        updateUserCompleto(updatedUser) { success ->
                            if (success) {
                                println("Usuário atualizado com sucesso")
                                usersList = usersList.map {
                                    if (it.login == updatedUser.login) it.copy(name = updatedUser.name) else it
                                }
                            } else {
                                println("Erro ao atualizar usuário")
                            }
                            showEditDialog = false
                        }
                    }
                }
            )
        }

        // Diálogo para remoção
        if (showRemoveDialog && selectedUser != null) {
            RemoveUserDialog(
                user = selectedUser!!,
                onDismiss = { showRemoveDialog = false },
                onConfirm = {
                    scope.launch {
                        removeUser(selectedUser!!.login) {
                            usersList = usersList.filter { it.login != selectedUser!!.login }
                            showRemoveDialog = false
                        }
                    }
                }
            )
        }
    }
}