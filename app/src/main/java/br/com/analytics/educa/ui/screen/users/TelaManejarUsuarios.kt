package br.com.analytics.educa.ui.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.ui.component.Footer
import br.com.analytics.educa.ui.component.Header
import br.com.analytics.educa.ui.component.dialog.AddUserDialog
import br.com.analytics.educa.ui.component.lists.AlunosList
import br.com.analytics.educa.ui.component.lists.TurmasList
import br.com.analytics.educa.ui.component.lists.TipoUsuarioList
import br.com.analytics.educa.ui.component.lists.UsuarioList

@Composable
fun TelaManejarUsuarios(
    navigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var selectedUserType by remember { mutableStateOf<String?>(null) }
    var selectedUser by remember { mutableStateOf<String?>(null) }
    var showAlunoList by remember { mutableStateOf(false) }
    var selectedTurma by remember { mutableStateOf<String?>(null) }
    var alunosByTurma by remember { mutableStateOf<List<String>>(emptyList()) }

    val turmas = listOf("Turma 1", "Turma 2", "Turma 3")
    val usuariosMockados = mapOf(
        "ALUNO" to listOf("Aluno 1", "Aluno 2", "Aluno 3"),
        "PROFESSOR" to listOf("Professor 1", "Professor 2"),
        "FUNCIONARIO" to listOf("Funcionario 1", "Funcionario 2"),
        "DIRETOR" to listOf("Diretor 1")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header fixado no topo
            Header(
                onShowAddDialog = { showAddDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento entre o cabeçalho e o conteúdo

            // Conteúdo principal
            when {
                selectedUserType == null -> {
                    TipoUsuarioList(
                        onUserTypeSelected = { userType -> selectedUserType = userType }
                    )
                }

                selectedUserType == "ALUNO" -> {
                    if (!showAlunoList) {
                        TurmasList(
                            turmas = turmas,
                            onTurmaSelected = { turma ->
                                selectedTurma = turma
                                alunosByTurma = usuariosMockados["ALUNO"].orEmpty()
                                showAlunoList = true
                            },
                            onBackToUserTypeSelection = { selectedUserType = null }
                        )
                    } else {
                        AlunosList(
                            alunos = alunosByTurma,
                            onAlunoSelected = { aluno ->
                                selectedUser = aluno
                                showEditDialog = true
                            },
                            onBackToTurmas = { showAlunoList = false }
                        )
                    }
                }

                else -> {
                    UsuarioList(
                        usuarios = usuariosMockados[selectedUserType] ?: emptyList(),
                        onUsuarioSelected = { usuario ->
                            selectedUser = usuario
                            showEditDialog = true
                        },
                        onBack = { selectedUserType = null }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Preenche espaço para empurrar o Footer para baixo
        }

        // Footer fixado na parte inferior
        Footer(
            showAlunoList = showAlunoList,
            selectedUserType = selectedUserType,
            navigateBack = navigateBack,
            onBackToTurmas = { showAlunoList = false },
            onResetUserType = { selectedUserType = null },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }

    // Dialogs
    if (showAddDialog) {
        AddUserDialog(
            onDismiss = { showAddDialog = false }
        )
    }

    if (showEditDialog) {
        // Dialog de edição (defina sua implementação aqui)
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Usuário") },
            text = { Text("Aqui você pode editar informações do usuário: $selectedUser.") },
            confirmButton = {
                Button(
                    onClick = {
                        println("Usuário editado: $selectedUser")
                        showEditDialog = false
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showRemoveDialog) {
        // Dialog de remoção (defina sua implementação aqui)
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remover Usuário") },
            text = { Text("Deseja remover o usuário: $selectedUser?") },
            confirmButton = {
                Button(
                    onClick = {
                        println("Usuário removido: $selectedUser")
                        showRemoveDialog = false
                    }
                ) {
                    Text("Remover")
                }
            },
            dismissButton = {
                Button(onClick = { showRemoveDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
