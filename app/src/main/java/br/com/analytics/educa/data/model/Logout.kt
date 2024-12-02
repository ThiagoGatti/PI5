package br.com.analytics.educa.data.model

import android.content.Context
import android.widget.Toast

fun performLogout(context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    Toast.makeText(context, "Usuário deslogado!", Toast.LENGTH_SHORT).show()
}