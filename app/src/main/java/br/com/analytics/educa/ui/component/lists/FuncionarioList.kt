package br.com.analytics.educa.ui.component.lists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
