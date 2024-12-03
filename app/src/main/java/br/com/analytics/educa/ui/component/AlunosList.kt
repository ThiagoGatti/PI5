package br.com.analytics.educa.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlunosList(
    alunos: List<String>,
    turma: String,
    onAlunoSelected: (String) -> Unit,
    onBackToTurmas: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Turma Selecionada: $turma",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )


        alunos.forEach { aluno ->
            Button(
                onClick = { onAlunoSelected(aluno) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(aluno)
            }
        }

        Button(
            onClick = { onBackToTurmas() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Voltar para Seleção de Turma")
        }
    }
}
