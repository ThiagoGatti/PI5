package br.com.analytics.educa.ui.component.lists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

