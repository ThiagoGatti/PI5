package br.com.analytics.educa.ui.component.dialog

import androidx.compose.runtime.Composable

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
