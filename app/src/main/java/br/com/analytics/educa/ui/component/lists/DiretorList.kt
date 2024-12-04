package br.com.analytics.educa.ui.component.lists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
