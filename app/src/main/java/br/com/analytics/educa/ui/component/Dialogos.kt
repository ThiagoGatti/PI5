package br.com.analytics.educa.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.ui.screen.users.UserForm

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
    selectedUser: String?,
    onDismissAdd: () -> Unit,
    onDismissEdit: () -> Unit,
    onDismissRemove: () -> Unit
) {
    if (showAddDialog) {
        AddUserDialog(onDismiss = onDismissAdd)
    }

    if (showEditDialog) {
        EditUserDialog(
            selectedUser = selectedUser,
            onDismiss = onDismissEdit
        )
    }

    if (showRemoveDialog) {
        RemoveUserDialog(
            selectedUser = selectedUser,
            onDismiss = onDismissRemove
        )
    }
}

@Composable
fun EditUserDialog(
    selectedUser: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Usuário") },
        text = { Text("Aqui você pode editar informações do usuário: $selectedUser.") },
        confirmButton = {
            Button(
                onClick = {
                    println("Usuário editado: $selectedUser")
                    onDismiss()
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
    selectedUser: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remover Usuário") },
        text = { Text("Deseja remover o usuário: $selectedUser?") },
        confirmButton = {
            Button(
                onClick = {
                    println("Usuário removido: $selectedUser")
                    onDismiss()
                }
            ) {
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