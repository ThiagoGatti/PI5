package br.com.analytics.educa.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AlunosList(
    alunos: List<String>,
    onAlunoSelected: (String) -> Unit,
    onBackToTurmas: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onBackToTurmas,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Voltar para Turmas")
        }

        alunos.forEach { aluno ->
            Button(
                onClick = { onAlunoSelected(aluno) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(aluno)
            }
        }
    }
}

@Composable
fun DiretorList(
    diretores: List<String>,
    onDiretorSelected: (String) -> Unit,
    onBackToUserTypeSelection: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onBackToUserTypeSelection,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Voltar para seleção de tipo de usuário")
        }

        diretores.forEach { diretor ->
            Button(
                onClick = { onDiretorSelected(diretor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(diretor)
            }
        }
    }
}

@Composable
fun FuncionarioList(
    funcionarios: List<String>,
    onFuncionarioSelected: (String) -> Unit,
    onBackToUserTypeSelection: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onBackToUserTypeSelection,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Voltar para seleção de tipo de usuário")
        }

        funcionarios.forEach { funcionario ->
            Button(
                onClick = { onFuncionarioSelected(funcionario) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(funcionario)
            }
        }
    }
}

@Composable
fun ProfessorList(
    professores: List<String>,
    onProfessorSelected: (String) -> Unit,
    onBackToUserTypeSelection: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onBackToUserTypeSelection,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Voltar para seleção de tipo de usuário")
        }

        professores.forEach { professor ->
            Button(
                onClick = { onProfessorSelected(professor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(professor)
            }
        }
    }
}

@Composable
fun TipoUsuarioList(
    onUserTypeSelected: (String) -> Unit
) {
    val tipoOptions = listOf("ALUNO", "PROFESSOR", "FUNCIONARIO", "DIRETOR")
    Text(
        text = "Selecione o tipo de usuário:",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    tipoOptions.forEach { tipo ->
        Button(
            onClick = { onUserTypeSelected(tipo) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = tipo, color = Color.White)
        }
    }
}

@Composable
fun TurmasList(
    turmas: List<String>,
    onTurmaSelected: (String) -> Unit,
    onBackToUserTypeSelection: () -> Unit // Novo parâmetro
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Botão para voltar à seleção de tipo de usuário
        Button(
            onClick = onBackToUserTypeSelection,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Voltar para seleção de tipo de usuário", color = Color.White)
        }

        // Listagem de turmas
        turmas.forEach { turma ->
            Button(
                onClick = { onTurmaSelected(turma) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = turma, color = Color.White)
            }
        }
    }
}

@Composable
fun UsuarioList(
    usuarios: List<String>,
    onUsuarioSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    Column {
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Voltar")
        }

        usuarios.forEach { usuario ->
            Button(
                onClick = { onUsuarioSelected(usuario) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = usuario, color = Color.White)
            }
        }
    }
}