import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.data.database.DatabaseHelper
import br.com.analytics.educa.data.database.UsuarioRepository
import br.com.analytics.educa.ui.route.Route
import kotlinx.coroutines.delay

@Composable
fun UserVerification(
    username: String,
    password: String,
    navigateToMenu: (String) -> Unit,
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var loginFailed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        val nome = verifyLoginWithDatabase(username, password)
        isLoading = false
        if (nome != null) {
            Toast.makeText(context, "Bem-vindo, $nome!", Toast.LENGTH_SHORT).show()
            navigateToMenu(Route.menuAluno)
        } else {
            loginFailed = true
            navigateToLogin()
        }
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
        } else if (loginFailed) {
            Text(
                text = "Falha no login. Tente novamente.",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun verifyLoginWithDatabase(username: String, password: String): String? {
    val dbHelper = DatabaseHelper()
    val usuarioRepo = UsuarioRepository(dbHelper)
    return if (usuarioRepo.autenticarUsuario(username, password)) {
        usuarioRepo.obterNomeUsuario(username)
    } else {
        null
    }
}