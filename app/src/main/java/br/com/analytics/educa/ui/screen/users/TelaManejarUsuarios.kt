package br.com.analytics.educa.ui.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import br.com.analytics.educa.ui.component.AddUserDialog
import br.com.analytics.educa.ui.component.EditUserDialog
import br.com.analytics.educa.ui.component.RemoveUserDialog
import kotlinx.coroutines.launch

@Composable
fun TelaManejarUsuarios(
    currentUserLogin: String,
    navigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }


    var contentState by remember { mutableStateOf("Tipo") } // Estados: Tipo, Turma, Usuarios
    var selectedUserType by remember { mutableStateOf<String?>(null) }
    var selectedTurma by remember { mutableStateOf<String?>(null) }
    var selectedUser by remember { mutableStateOf<UserCompleto?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showActionDialog by remember { mutableStateOf(false) }
    var usersList by remember { mutableStateOf<List<User>>(emptyList()) }
    var turmasList by remember { mutableStateOf<List<String>>(emptyList()) }

    val scope = rememberCoroutineScope()

    // Carregar turmas ao iniciar
    LaunchedEffect(Unit) {
        fetchTurmas { turmas -> turmasList = turmas }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gerenciar Usuários",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar usuário",
                        tint = Color.White
                    )
                }
            }
        }
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
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título fixo
            Text(
                text = when (contentState) {
                    "Tipo" -> "Selecione o Tipo de Usuário"
                    "Turma" -> "Selecione a Turma"
                    else -> "Usuários"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            // Conteúdo dinâmico baseado no estado
            when (contentState) {
                "Tipo" -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Botões de tipo de usuário
                        listOf(
                            "Aluno",
                            "Professor",
                            "Funcionário",
                            "Diretor"
                        ).forEach { type ->
                            Button(
                                onClick = {
                                    selectedUserType = type
                                    contentState =
                                        if (type == "Aluno") "Turma" else "Usuarios"
                                    if (type != "Aluno") {
                                        fetchUsersByType(type) { users ->
                                            usersList = users
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.7f)
                            ) {
                                Text(type)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Botão "Voltar" visível somente na seleção de tipo
                        Button(
                            onClick = navigateBack,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF5D145B
                                )
                            ),
                            modifier = Modifier
                                .width(160.dp)
                                .height(60.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = "Ícone de voltar",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Voltar",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                "Turma" -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        turmasList.forEach { turma ->
                            Button(
                                onClick = {
                                    selectedTurma = turma
                                    contentState = "Usuarios"
                                    fetchUsersByTurma(turma) { users -> usersList = users }
                                },
                                modifier = Modifier.fillMaxWidth(0.7f)
                            ) {
                                Text(turma)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Botão para voltar à seleção de tipo
                        Button(
                            onClick = { contentState = "Tipo" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF5D145B
                                )
                            ),
                            modifier = Modifier
                                .width(160.dp)
                                .height(60.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = "Ícone de voltar",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Voltar",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                "Usuarios" -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        usersList.forEach { user ->
                            val isCurrentUser = user.login == currentUserLogin
                            Button(
                                onClick = {
                                    if (!isCurrentUser) {
                                        scope.launch {
                                            fetchUserCompleto(user.login, { fetchedUser ->
                                                selectedUser = fetchedUser
                                                showActionDialog = true
                                            }, { error ->
                                                println("Erro ao buscar usuário completo: $error")
                                            })
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.7f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCurrentUser) Color.Gray else MaterialTheme.colorScheme.primary,
                                    contentColor = if (isCurrentUser) Color.DarkGray else Color.White
                                ),
                                enabled = !isCurrentUser // Desabilitar o botão se for o usuário atual
                            ) {
                                Text("${user.name} (${user.login})")
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Botão para voltar à seleção anterior
                        Button(
                            onClick = {
                                contentState =
                                    if (selectedUserType == "Aluno") "Turma" else "Tipo"
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF5D145B
                                )
                            ),
                            modifier = Modifier
                                .width(160.dp)
                                .height(60.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = "Ícone de voltar",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Voltar",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddUserDialog(
                onDismiss = { showAddDialog = false },
                onUserCreated = { newUser ->
                    // Atualize a lista de usuários ou execute qualquer ação necessária
                    println("Novo usuário criado: $newUser")
                }
            )
        }

        // Diálogo de Ações
        if (showActionDialog && selectedUser != null) {
            ActionDialog(
                user = selectedUser!!,
                onEdit = {
                    showEditDialog = true
                    showActionDialog = false
                },
                onRemove = {
                    showRemoveDialog = true
                    showActionDialog = false
                },
                onDismiss = { showActionDialog = false }
            )
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
                            usersList =
                                usersList.filter { it.login != selectedUser!!.login }
                            showRemoveDialog = false
                        }
                    }
                }
            )
        }
    }
}