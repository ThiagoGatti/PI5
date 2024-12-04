package br.com.analytics.educa.ui.component.lists


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
