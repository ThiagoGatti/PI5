package br.com.analytics.educa.ui.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var contentState by remember { mutableStateOf("Tipo") }
    var selectedUserType by remember { mutableStateOf<String?>(null) }
    var selectedTurma by remember { mutableStateOf<String?>(null) }
    var selectedUser by remember { mutableStateOf<UserCompleto?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showActionDialog by remember { mutableStateOf(false) }
    var usersList by remember { mutableStateOf<List<User>>(emptyList()) }
    var turmasList by remember { mutableStateOf<List<String>>(emptyList()) }

    val scope = rememberCoroutineScope()

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
    ) {
        // Botão "+"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 60.dp), // Ajuste de padding vertical para descer
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                modifier = Modifier
                    .background(Color(0xFF5D145B), shape = MaterialTheme.shapes.medium)
                    .size(48.dp),
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar usuário",
                    tint = Color.White
                )
            }
        }

// Texto "Selecione o Tipo de Usuário"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 150.dp), // Maior espaçamento para evitar sobreposição
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (contentState == "Tipo") {
                Text(
                    text = "Selecione o Tipo de Usuário",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 1.dp) // Espaçamento adicional
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (contentState) {
                    "Tipo" -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            listOf("Aluno", "Professor", "Funcionário", "Diretor").forEach { type ->
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
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .height(70.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
                                            text = type,
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    "Turma" -> {
                        if (turmasList.isEmpty()) {
                            Text("Nenhuma turma disponível.", color = Color.White)
                        } else {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize().padding(bottom = 80.dp) // Espaço para o botão Voltar
                            ) {
                                items(turmasList) { turma ->
                                    Button(
                                        onClick = {
                                            selectedTurma = turma
                                            contentState = "Usuarios"
                                            fetchUsersByTurma(turma) { users -> usersList = users }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .height(70.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
                                                text = turma,
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "Usuarios" -> {
                        if (usersList.isEmpty()) {
                            Text("Nenhum usuário encontrado.", color = Color.White)
                        } else {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize().padding(bottom = 80.dp) 
                            ) {
                                items(usersList) { user ->
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
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .height(70.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        contentPadding = PaddingValues()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    color = if (isCurrentUser) Color.Gray else Color(0xFF5D145B),
                                                    shape = RoundedCornerShape(16.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${user.name} (${user.login})",
                                                color = if (isCurrentUser) Color.DarkGray else Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            Button(
                    onClick = {
                        when (contentState) {
                            "Tipo" -> navigateBack()
                            "Turma" -> contentState = "Tipo"
                            else -> contentState =
                                if (selectedUserType == "Aluno") "Turma" else "Tipo"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.6f)
                        .height(75.dp)
                        .padding(bottom = 16.dp) // Espaço inferior
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
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }



        if (showAddDialog) {
            AddUserDialog(
                turmasList = turmasList,
                onDismiss = { showAddDialog = false },
                onUserCreated = { newUser ->
                    println("Novo usuário criado: $newUser")
                }
            )
        }

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

        if (showEditDialog && selectedUser != null) {
            EditUserDialog(
                user = selectedUser!!,
                turmasList = turmasList,
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
}


