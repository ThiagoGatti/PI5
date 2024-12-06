package br.com.analytics.educa.ui.component.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.retrofit.UserCompleto
import br.com.analytics.educa.ui.component.entradaTextoCPF
import br.com.analytics.educa.ui.component.formatDateFromDatabase
import br.com.analytics.educa.ui.component.formatDateToDatabase
import br.com.analytics.educa.ui.component.formaterValidarDataCursor
import br.com.analytics.educa.ui.component.phoneNumberFormatter
import br.com.analytics.educa.ui.component.validarData

@Composable
fun EditUserDialog(
    user: UserCompleto,
    turmasList: List<String>,
    onDismiss: () -> Unit,
    onSave: (UserCompleto) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var cpfState by remember { mutableStateOf(TextFieldValue(user.cpf)) }
    var birthDateState by remember {
        mutableStateOf(
            TextFieldValue(formatDateFromDatabase(user.birthDate))
        )
    }
    var phone by remember { mutableStateOf(user.phone) }
    var phoneCursorPosition by remember { mutableStateOf(0) }
    var password by remember { mutableStateOf("") }
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

                entradaTextoCPF(
                    value = cpfState,
                    onValueChange = { cpfState = it },
                    modifier = Modifier.fillMaxWidth()
                )
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
                    singleLine = true
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
                    singleLine = true
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
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Nova Senha (Deixe em branco para manter a atual)") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
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
                            cpf = cpfState.text,
                            birthDate = formatDateToDatabase(birthDateState.text),
                            phone = phone,
                            password = if (password.isNotEmpty()) password else "",
                            components = components
                        )
                    )
                    println(user.copy(
                        action = "updateUserCompleto",
                        name = name,
                        cpf = cpfState.text,
                        birthDate = formatDateToDatabase(birthDateState.text),
                        phone = phone,
                        password = if (password.isNotEmpty()) password else "",
                        components = components
                    ))
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