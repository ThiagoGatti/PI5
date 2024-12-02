package br.com.analytics.educa.ui.menu

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Busca os formulários já respondidos
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

    // Gradiente de fundo e layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Formulários Disponíveis",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            // Botões de formulários
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
                        containerColor = if (isAnswered) Color.Red else Color(0xFF641864),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = formName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botão "Voltar"
            Button(
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF641864)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Voltar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
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
