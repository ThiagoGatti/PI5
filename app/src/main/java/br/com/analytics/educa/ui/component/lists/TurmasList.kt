package br.com.analytics.educa.ui.component.lists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

