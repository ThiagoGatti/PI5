package br.com.analytics.educa.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuForm(
    userType: String,
    navigateToForm: (String) -> Unit,
    navigateBack: () -> Unit
) {
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
            Button(
                onClick = { navigateToForm(formName) },
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