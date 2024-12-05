package br.com.analytics.educa.ui.component.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.analytics.educa.data.retrofit.UserCompleto

@Composable
fun RemoveUserDialog(
    user: UserCompleto,
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