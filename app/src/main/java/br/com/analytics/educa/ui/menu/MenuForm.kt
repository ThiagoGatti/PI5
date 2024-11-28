package br.com.analytics.educa.ui.menu

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.retrofit.ApiService
import br.com.analytics.educa.data.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MenuForm(
    userType: String,
    username: String,
    navigateToForm: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estado para armazenar formulários já respondidos
    var answeredForms by remember { mutableStateOf(setOf<String>()) }

    // Buscar formulários já respondidos ao carregar a tela
    LaunchedEffect(userType, username) {
        coroutineScope.launch {
            fetchAnsweredForms(userType, username) { answered ->
                answeredForms = answered
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Formulários Disponíveis",
            modifier = Modifier.padding(bottom = 24.dp)
        )

        forms(userType).forEach { formName ->
            val isAnswered = answeredForms.contains(formName)
            Button(
                onClick = {
                    if (isAnswered) {
                        Toast.makeText(context, "Formulário já respondido.", Toast.LENGTH_SHORT).show()
                    } else {
                        navigateToForm(formName)
                    }
                },
                enabled = !isAnswered,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAnswered) Color.Gray else Color(0xFF6850A3),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Formulário - $formName")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = navigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Voltar")
        }
    }
}

// Função para buscar formulários já respondidos
fun fetchAnsweredForms(
    userType: String,
    login: String,
    onResult: (Set<String>) -> Unit
) {
    val apiService = RetrofitClient.createService(ApiService::class.java)
    apiService.getAnsweredForms(userType, login).enqueue(object : Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            if (response.isSuccessful) {
                onResult(response.body()?.toSet() ?: emptySet())
            } else {
                onResult(emptySet())
            }
        }

        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            onResult(emptySet())
        }
    })
}

// Função para retornar os formulários com base no tipo de usuário
fun forms(userType: String): List<String> {
    return when (userType) {
        "ALUNO" -> listOf(
            "Autonomia e Protagonismo",
            "Clima Escolar",
            "Qualidade do Ensino",
            "Infraestrutura",
            "Gestão"
        )
        "PROFESSOR" -> listOf(
            "Condições de Trabalho",
            "Qualidade da Educação",
            "Clima Escolar",
            "Participação"
        )
        "FUNCIONARIO" -> listOf(
            "Satisfação no Trabalho",
            "Eficiência da Gestão",
            "Infraestrutura"
        )
        else -> emptyList()
    }
}