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
import br.com.analytics.educa.data.model.buscarFormsRespondidos

@Composable
fun MenuForm(
    userType: String,
    username: String,
    navigateToForm: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var answeredForms by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(userType, username) {
        buscarFormsRespondidos(
            userType = userType,
            login = username,
            onResult = { answered ->
                answeredForms = answered
            },
            onError = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
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