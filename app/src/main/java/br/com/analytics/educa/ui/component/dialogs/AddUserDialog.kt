package br.com.analytics.educa.ui.component.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.model.createUserCompleto
import br.com.analytics.educa.data.retrofit.UserCompleto
import br.com.analytics.educa.ui.component.entradaTextoCPF
import br.com.analytics.educa.ui.component.formatDateToDatabase
import br.com.analytics.educa.ui.component.formaterValidarDataCursor
import br.com.analytics.educa.ui.component.phoneNumberFormatter
import br.com.analytics.educa.ui.component.validarData
import br.com.analytics.educa.ui.screen.users.fields.DropdownField
import br.com.analytics.educa.ui.screen.users.fields.SpecificUserFields

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(
    turmasList: List<String>,
    onDismiss: () -> Unit,
    onUserCreated: (UserCompleto) -> Unit
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(1) }
    var userType by remember { mutableStateOf<String?>(null) }
    var login by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var cpfState by remember { mutableStateOf(TextFieldValue("")) } // Usando TextFieldValue para CPF
    var birthDateState by remember { mutableStateOf(TextFieldValue("")) } // Usando TextFieldValue para data
    var phone by remember { mutableStateOf("") }
    var phoneCursorPosition by remember { mutableStateOf(0) }
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
                        value = cpfState,
                        onValueChange = { cpfState = it }, // Atualiza o estado do CPF
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = birthDateState,
                        onValueChange = { input ->
                            val (formattedDate, newCursorPos) = formaterValidarDataCursor(
                                input.text,
                                input.selection.start
                            )
                            birthDateState = TextFieldValue(
                                text = formattedDate,
                                selection = TextRange(newCursorPos)
                            )
                        },
                        label = { Text("Data de Nascimento (DD/MM/YYYY)") },
                        isError = !validarData(birthDateState.text) && birthDateState.text.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    if (!validarData(birthDateState.text) && birthDateState.text.isNotEmpty()) {
                        Text(
                            text = "Data inválida. Verifique o dia, mês ou ano.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = TextFieldValue(phone, TextRange(phoneCursorPosition)),
                        onValueChange = { input ->
                            val (formattedPhone, newCursorPosition) =
                                phoneNumberFormatter(input.text, input.selection.start)
                            phone = formattedPhone
                            phoneCursorPosition = newCursorPosition
                        },
                        label = { Text("Telefone (Ex.: (XX) XXXXX-XXXX)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
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
                            cpf = cpfState.text,
                            birthDate = formatDateToDatabase(birthDateState.text),
                            phone = phone,
                            type = userType!!.toUpperCase(),
                            password = senha,
                            components = components
                        )

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