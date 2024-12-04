package br.com.analytics.educa.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    onShowAddDialog: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Gerenciar Usuários",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )

        IconButton(
            onClick = onShowAddDialog,
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar Usuário",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}



@Composable
fun Footer(
    showAlunoList: Boolean,
    selectedUserType: String?,
    navigateBack: () -> Unit,
    onBackToTurmas: () -> Unit,
    onResetUserType: () -> Unit,
    modifier: Modifier = Modifier // Novo parâmetro para personalização do layout
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), // Adiciona espaçamento inferior
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                when {
                    showAlunoList -> onBackToTurmas()
                    selectedUserType != null -> onResetUserType()
                    else -> navigateBack()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Voltar",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

