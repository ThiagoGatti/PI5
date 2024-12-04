package br.com.analytics.educa.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.ui.component.lists.AlunosList
import br.com.analytics.educa.ui.component.lists.ProfessorList
import br.com.analytics.educa.ui.component.lists.FuncionarioList
import br.com.analytics.educa.ui.component.lists.DiretorList
import br.com.analytics.educa.ui.component.formatters.CpfTextField
import br.com.analytics.educa.ui.component.lists.TurmasList
import br.com.analytics.educa.ui.component.formatters.formatPhoneNumberPreservingCursor
import formatAndValidateDatePreservingCursor
import isValidDate

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
