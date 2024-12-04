package br.com.analytics.educa.ui.component.lists
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
