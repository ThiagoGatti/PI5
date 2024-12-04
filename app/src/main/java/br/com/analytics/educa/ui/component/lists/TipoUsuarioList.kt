package br.com.analytics.educa.ui.component.lists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
