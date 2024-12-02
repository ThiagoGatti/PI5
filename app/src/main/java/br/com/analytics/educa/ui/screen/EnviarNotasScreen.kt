package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun EnviarNotasScreen(
    navigateBack: () -> Unit,
    turmas: List<String>, // Lista de turmas recebida como parâmetro
    getAlunosByTurma: (String) -> List<String>, // Função para buscar alunos por turma
    enviarNotaEFrequencia: (String, String, Double, Int) -> Unit // Função para enviar os dados
) {
    val context = LocalContext.current

    // Estados para controlar seleção de turma e lista de alunos
    var turmaSelecionada by remember { mutableStateOf<String?>(null) }
    var alunos by remember { mutableStateOf<List<String>>(emptyList()) }

    // Estados para controlar nota e frequência de cada aluno
    val notas = remember { mutableStateMapOf<String, Double>() }
    val frequencias = remember { mutableStateMapOf<String, Int>() }

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
                .padding(16.dp)
        ) {
            // Título da tela
            Text(
                text = "Enviar Notas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )

            // Lista de turmas ou lista de alunos
            if (turmaSelecionada == null) {
                Text(
                    text = "Selecione uma turma",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn {
                    items(turmas) { turma ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    turmaSelecionada = turma
                                    alunos = getAlunosByTurma(turma)
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Text(
                                text = turma,
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Turma: $turmaSelecionada",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn {
                    items(alunos) { aluno ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = aluno,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = notas[aluno]?.toString() ?: "",
                                    onValueChange = {
                                        notas[aluno] = it.toDoubleOrNull() ?: 0.0
                                    },
                                    label = { Text("Nota") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = frequencias[aluno]?.toString() ?: "",
                                    onValueChange = {
                                        frequencias[aluno] = it.toIntOrNull() ?: 0
                                    },
                                    label = { Text("Frequência (%)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        val nota = notas[aluno] ?: 0.0
                                        val frequencia = frequencias[aluno] ?: 0
                                        enviarNotaEFrequencia(turmaSelecionada!!, aluno, nota, frequencia)
                                        Toast.makeText(context, "Dados enviados para $aluno", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Enviar Dados")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { turmaSelecionada = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Voltar para seleção de turma")
                }
            }
        }

        // Botão de voltar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Voltar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}
