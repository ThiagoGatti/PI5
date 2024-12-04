package br.com.analytics.educa.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TelaDadosPessoais(
    username: String,
    userType: String,
    navigateBack: () -> Unit
) {
    val userInfo = when (userType.uppercase()) {
        "ALUNO" -> listOf(
            Pair(Icons.Default.Person, "Usuário: $username"),
            Pair(Icons.Default.Person, "Nome Completo: João da Silva"),
            Pair(Icons.Default.ListAlt, "Matricula: 123456"),
            Pair(Icons.Default.Book, "Turma: 3B"),
            Pair(Icons.Default.Phone, "Telefone: (11) 99920-0424"),
            Pair(Icons.Default.Person, "CPF: 123.456.789-00")
        )
        "PROFESSOR" -> listOf(
            Pair(Icons.Default.Person, "Usuário: $username"),
            Pair(Icons.Default.Person, "Nome Completo: Maria Oliveira"),
            Pair(Icons.Default.Book, "Disciplina: Matemática"),
            Pair(Icons.Default.ListAlt, "Turmas Associadas: 1A, 2B"),
            Pair(Icons.Default.Phone, "Telefone: (11) 98765-4321"),
            Pair(Icons.Default.Person, "CPF: 987.654.321-00")
        )
        "FUNCIONARIO" -> listOf(
            Pair(Icons.Default.Person, "Usuário: $username"),
            Pair(Icons.Default.Person, "Nome Completo: Carlos Pereira"),
            Pair(Icons.Default.Home, "Função: Secretaria"),
            Pair(Icons.Default.Phone, "Telefone: (11) 99876-5432"),
            Pair(Icons.Default.Person, "CPF: 987.654.321-00")
        )
        "DIRETOR" -> listOf(
            Pair(Icons.Default.Person, "Usuário: $username"),
            Pair(Icons.Default.Person, "Nome Completo: Ana Souza"),
            Pair(Icons.Default.Home, "Setor: Administração Geral"),
            Pair(Icons.Default.Phone, "Telefone: (11) 91234-5678")
        )
        else -> listOf(
            Pair(Icons.Default.Person, "Usuário: $username"),
            Pair(Icons.Default.Person, "Tipo de Usuário: Não identificado")
        )
    }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Dados Pessoais",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                userInfo.forEach { (icon, info) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = info,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botão de Voltar
            Button(
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
                modifier = Modifier
                    .width(160.dp)
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Ícone de voltar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
}
