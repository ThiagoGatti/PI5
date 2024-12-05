package br.com.analytics.educa.ui.component

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.createUserCompleto
import br.com.analytics.educa.data.retrofit.UserCompleto
import br.com.analytics.educa.ui.screen.users.fields.DropdownField
import br.com.analytics.educa.ui.screen.users.fields.SpecificUserFields
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(
    turmasList : List<String>,
    onDismiss: () -> Unit,
    onUserCreated: (UserCompleto) -> Unit
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(1) }
    var userType by remember { mutableStateOf<String?>(null) }
    var login by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var components by remember { mutableStateOf(mapOf<String, Any>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (step == 1) "Informações de Login" else "Cadastrar Novo Usuário") },
        text = {
            if (step == 1) {
                Column {
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
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            } else {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    entradaTextoCPF(
                        value = TextFieldValue(cpf),
                        onValueChange = { cpf = it.text },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        label = { Text("Data de Nascimento") },
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
                    DropdownField(
                        label = "Tipo de Usuário",
                        options = listOf("Aluno", "Professor", "Funcionario", "Diretor"),
                        selectedOption = userType.orEmpty(),
                        onOptionSelected = { userType = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (!userType.isNullOrEmpty()) {
                        SpecificUserFields(
                            userType = userType!!,
                            turmasList = turmasList,
                            onFieldsUpdated = { updatedComponents ->
                                components = updatedComponents
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (step == 1) {
                        step = 2
                    } else {
                        val newUser = UserCompleto(
                            action = "createUserCompleto",
                            login = login,
                            name = name,
                            cpf = cpf,
                            birthDate = formatDateToDatabase(birthDate),
                            phone = phone,
                            type = userType!!.toUpperCase(),
                            password = senha,
                            components = components
                        )

                        println("Usuário a ser criado: $newUser")

                        // Enviar usuário ao backend
                        createUserCompleto(newUser) { success, message ->
                            if (success) {
                                Toast.makeText(context, "Usuário $login criado com sucesso!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            } else {
                                println("Erro ao criar usuário: $message")
                            }
                        }
                    }
                }
            ) {
                Text(if (step == 1) "Próximo" else "Salvar")
            }
        },
        dismissButton = {
            Button(onClick = { if (step == 1) onDismiss() else step = 1 }) {
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
    selectedUser: UserCompleto?,
    turmasList: List<String>,
    onDismissAdd: () -> Unit,
    onDismissEdit: () -> Unit,
    onDismissRemove: () -> Unit,
    onUserCreated: (UserCompleto) -> Unit // Adicionado o parâmetro necessário
) {
    if (showAddDialog) {
        AddUserDialog(
            turmasList = turmasList,
            onDismiss = onDismissAdd,
            onUserCreated = onUserCreated // Passando o parâmetro ausente
        )
    }

    if (showEditDialog && selectedUser != null) {
        EditUserDialog(
            user = selectedUser,
            onDismiss = onDismissEdit,
            turmasList = turmasList,
            onSave = { updatedUser ->
                println("Usuário atualizado: $updatedUser")
            }
        )
    }

    if (showRemoveDialog && selectedUser != null) {
        RemoveUserDialog(
            user = selectedUser,
            onDismiss = onDismissRemove,
            onConfirm = {
                println("Usuário removido: ${selectedUser.login}")
            }
        )
    }
}

@Composable
fun EditUserDialog(
    user: UserCompleto,
    turmasList: List<String>,
    onDismiss: () -> Unit,
    onSave: (UserCompleto) -> Unit
) {
    // Estados para os campos básicos
    var name by remember { mutableStateOf(user.name) }
    var cpf by remember { mutableStateOf(user.cpf) }
    var birthDate by remember {
        mutableStateOf(formatDateFromDatabase(user.birthDate))
    }
    var phone by remember { mutableStateOf(user.phone) }
    var password by remember { mutableStateOf("") } // Novo estado para a senha
    var components by remember { mutableStateOf(mapOf<String, Any>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Usuário") },
        text = {
            Column {
                // Nome
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CPF
                entradaTextoCPF(
                    value = TextFieldValue(cpf),
                    onValueChange = { cpf = it.text },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Telefone
                OutlinedTextField(
                    value = TextFieldValue(phone),
                    onValueChange = { input ->
                        val (formatted, cursor) = formaterCelular(input.text, input.selection.start)
                        phone = formatted
                    },
                    label = { Text("Telefone (Ex.: (XX) XXXXX-XXXX)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Data de Nascimento
                OutlinedTextField(
                    value = TextFieldValue(birthDate),
                    onValueChange = { input ->
                        val result = formaterValidarDataCursor(input.text, input.selection.start)
                        if (result != null) {
                            birthDate = result.first
                        }
                    },
                    label = { Text("Data de Nascimento (DD/MM/YYYY)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Senha (Opcional)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Nova Senha (Deixe em branco para manter a atual)") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                SpecificUserFields(
                    initialValues = user.components,
                    turmasList = turmasList,
                    userType = user.type.first().toUpperCase() + user.type.substring(1).toLowerCase(),
                    onFieldsUpdated = { updatedComponents ->
                        components = updatedComponents
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        user.copy(
                            action = "updateUserCompleto",
                            name = name,
                            cpf = cpf,
                            birthDate = formatDateToDatabase(birthDate),
                            phone = phone,
                            password = if (password.isNotEmpty()) password else null,
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

