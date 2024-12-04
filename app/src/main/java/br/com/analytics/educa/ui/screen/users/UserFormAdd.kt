package br.com.analytics.educa.ui.screen.users

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.ui.component.entradaTextoCPF
import br.com.analytics.educa.ui.component.formaterCelular
import br.com.analytics.educa.ui.screen.forms.SpecificUserFields
import formatAndValidateDatePreservingCursor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserForm(
    userType: String,
    onDismiss: () -> Unit,
    onError: (String?) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var cpfState by remember { mutableStateOf(TextFieldValue("")) }
    var dataNascimento by remember { mutableStateOf("") }
    var dataNascimentoCursor by remember { mutableStateOf(0) }
    var telefone by remember { mutableStateOf("") }
    var telefoneCursor by remember { mutableStateOf(0) }
    var login by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
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
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = TextFieldValue(telefone, TextRange(telefoneCursor)),
            onValueChange = { input ->
                val (formatted, cursor) = formaterCelular(input.text, input.selection.start)
                telefone = formatted
                telefoneCursor = cursor
            },
            label = { Text("Telefone (Ex.: (XX) XXXXX-XXXX)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colorScheme.primary
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

        // Campos específicos
        SpecificUserFields(userType = userType)
    }
}
