package br.com.analytics.educa.ui.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun Menu(
    userType: String,
    navigateToMenuForm: () -> Unit,
    navigateToInitialScreen: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Menu")
        Spacer(modifier = Modifier.height(16.dp))

        if (userType != "DIRETOR") {
            Button(
                onClick = navigateToMenuForm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Formulários Avaliativos")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                performLogout(context)
                navigateToInitialScreen()
            },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text("Deslogar")
        }
    }
}

fun performLogout(context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    Toast.makeText(context, "Usuário deslogado!", Toast.LENGTH_SHORT).show()
}