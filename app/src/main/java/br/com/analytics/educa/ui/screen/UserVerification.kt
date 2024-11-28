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
import br.com.analytics.educa.data.retrofit.ApiService
import br.com.analytics.educa.data.retrofit.LoginRequest
import br.com.analytics.educa.data.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.com.analytics.educa.ui.route.Route

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
            onSuccess = { mensagem ->
                isLoading = false
                loginMessage = mensagem
                Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show()
                navigateToMenu(Route.menuAluno)
            },
            onFailure = { erro ->
                isLoading = false
                loginMessage = erro
                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
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

fun autenticarUsuario(
    username: String,
    password: String,
    onSuccess: (String) -> Unit,
    onFailure: (String) -> Unit
) {
    val apiService = RetrofitClient.createService(ApiService::class.java)
    val loginRequest = LoginRequest(username, password)

    apiService.autenticarUsuario(loginRequest).enqueue(object : Callback<LoginRequest> {
        override fun onResponse(call: Call<LoginRequest>, response: Response<LoginRequest>) {
            if (response.isSuccessful) {
                val banco = response.body()
                banco?.let {
                    onSuccess("Bem-vindo, ${it.username}!")
                } ?: onFailure("Resposta inválida do servidor.")
            } else {
                onFailure("Erro no login: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<LoginRequest>, t: Throwable) {
            onFailure("Falha na conexão: ${t.message}")
        }
    })
}
