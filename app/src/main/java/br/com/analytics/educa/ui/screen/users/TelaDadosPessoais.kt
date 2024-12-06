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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import br.com.analytics.educa.data.model.fetchUserCompleto

@Composable
fun TelaDadosPessoais(
    username: String,
    navigateBack: () -> Unit
) {
    var userInfo by remember { mutableStateOf<List<Pair<ImageVector, String>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = username) {
        fetchUserCompleto(
            login = username,
            onResult = { userCompleto ->
                val infoList = mutableListOf<Pair<ImageVector, String>>(
                    Pair(Icons.Default.Person, "Usuário: ${userCompleto.login}"),
                    Pair(Icons.Default.Person, "Nome Completo: ${userCompleto.name}"),
                    Pair(Icons.Default.Phone, "Telefone: ${userCompleto.phone}"),
                    Pair(Icons.Default.Person, "CPF: ${userCompleto.cpf}")
                )

                when (userCompleto.type.uppercase()) {
                    "ALUNO" -> {
                        infoList.add(Pair(Icons.Default.ListAlt, "Turma: ${userCompleto.components["turma"]}"))
                    }
                    "PROFESSOR" -> {
                        infoList.add(Pair(Icons.Default.Book, "Disciplina: ${userCompleto.components["materia"]}"))
                        val turmas = (userCompleto.components["turmas"] as? List<*>)?.joinToString(", ")
                        infoList.add(Pair(Icons.Default.ListAlt, "Turmas Associadas: $turmas"))
                    }
                    "FUNCIONARIO" -> {
                        infoList.add(Pair(Icons.Default.Home, "Função: ${userCompleto.components["funcao"]}"))
                    }
                    "DIRETOR" -> {
                        infoList.add(Pair(Icons.Default.Home, "Setor: Administração Geral"))
                    }
                }

                userInfo = infoList
                loading = false
            },
            onError = { error ->
                errorMessage = error
                loading = false
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
            )
            .padding(16.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage.isNotEmpty()) {
            Text(
                text = "Erro: $errorMessage",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
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
}