package br.com.analytics.educa.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.retrofit.UserCompleto
import br.com.analytics.educa.ui.screen.users.fields.UserForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(1) } // Controle das etapas
    var userType by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (step == 1) "Selecionar Tipo de Usuário" else "Cadastrar Novo Usuário") },
        text = {
            if (step == 1) {
                Column {
                    val userTypes = listOf("Aluno", "Professor", "Funcionário", "Diretor")
                    Text("Selecione o tipo de usuário:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    userTypes.forEach { type ->
                        Button(
                            onClick = {
                                userType = type
                                step = 2 // Avança para a próxima etapa
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(type)
                        }
                    }
                }
            } else {
                Column {
                    userType?.let { type ->
                        UserForm(
                            userType = type,
                            onDismiss = onDismiss,
                            onError = { errorMessage = it }
                        )
                    }
                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        },
        confirmButton = {
            if (step == 2) {
                Button(
                    onClick = {
                        // Adicione lógica de validação e salvamento aqui
                        onDismiss()
                    }
                ) {
                    Text("Salvar")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    if (step == 2) {
                        step = 1 // Volta para a seleção do tipo de usuário
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text(if (step == 1) "Cancelar" else "Voltar")
            }
        }
    )
}

@Composable
fun DialogosUsuarios(
    showAddDialog: Boolean,
    showEditDialog: Boolean,
    showRemoveDialog: Boolean,
    selectedUser: UserCompleto?, // Corrigido para UserCompleto
    onDismissAdd: () -> Unit,
    onDismissEdit: () -> Unit,
    onDismissRemove: () -> Unit
) {
    if (showAddDialog) {
        AddUserDialog(onDismiss = onDismissAdd)
    }

    if (showEditDialog && selectedUser != null) {
        EditUserDialog(
            user = selectedUser,
            onDismiss = onDismissEdit,
            onSave = { updatedUser ->
                println("Usuário atualizado: $updatedUser") // Adicionar lógica para salvar
            }
        )
    }

    if (showRemoveDialog && selectedUser != null) {
        RemoveUserDialog(
            user = selectedUser,
            onDismiss = onDismissRemove,
            onConfirm = {
                println("Usuário removido: ${selectedUser.login}") // Adicionar lógica para remover
            }
        )
    }
}

@Composable
fun EditUserDialog(
    user: UserCompleto,
    onDismiss: () -> Unit,
    onSave: (UserCompleto) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var cpf by remember { mutableStateOf(user.cpf) }
    var birthDate by remember { mutableStateOf(user.birthDate) }
    var phone by remember { mutableStateOf(user.phone) }
    var idEscola by remember { mutableStateOf(user.idEscola.toString()) }
    var components by remember { mutableStateOf(user.components) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Usuário") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { cpf = it },
                    label = { Text("CPF") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = { Text("Data de Nascimento (DD/MM/YYYY)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = idEscola,
                    onValueChange = { idEscola = it },
                    label = { Text("ID da Escola") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (components.isNotEmpty()) {
                    Text("Componentes Específicos", style = MaterialTheme.typography.bodyMedium)
                    components.forEach { (key, value) ->
                        OutlinedTextField(
                            value = value.toString(),
                            onValueChange = {
                                components = components.toMutableMap().apply { this[key] = it }
                            },
                            label = { Text(key.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        user.copy(
                            name = name,
                            cpf = cpf,
                            birthDate = birthDate,
                            phone = phone,
                            idEscola = idEscola.toIntOrNull() ?: user.idEscola,
                            components = components
                        )
                    )
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun RemoveUserDialog(
    user: UserCompleto, // Atualizado para UserCompleto
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remover Usuário") },
        text = { Text("Tem certeza de que deseja remover o usuário ${user.name}?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Remover")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// Diálogo de Ações
@Composable
fun ActionDialog(
    user: UserCompleto,
    onEdit: () -> Unit,
    onRemove: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ações para ${user.name}") },
        text = { Text("Escolha uma ação:") },
        confirmButton = {
            Column {
                Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRemove, modifier = Modifier.fillMaxWidth()) {
                    Text("Remover")
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}