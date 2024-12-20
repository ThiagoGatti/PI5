import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import br.com.analytics.educa.data.model.autenticarUsuario
import br.com.analytics.educa.data.model.performLogout

@Composable
fun UserVerification(
    username: String,
    password: String,
    navigateToMenu: (String) -> Unit,
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var loginMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        autenticarUsuario(
            username = username,
            password = password,
            onSuccess = { nome, tipo ->
                isLoading = false
                val mensagem = "Bem-vindo, $nome!"
                Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show()
                println("$mensagem Tipo de usuário: $tipo")
                navigateToMenu(tipo)
            },
            onFailure = { erro ->
                isLoading = false
                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                println("Erro na autenticação: $erro")
                performLogout(context)
                navigateToLogin()
            }
        )

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            loginMessage?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}