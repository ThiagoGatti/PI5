package br.com.analytics.educa.ui.component.formatters

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun CpfTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val digitsOnly = input.text.filter { it.isDigit() } // Remove caracteres não numéricos
            if (digitsOnly.length <= 11) { // Limita a entrada a 11 dígitos
                val (formattedText, newCursorPosition) = formatCpfPreservingCursor(digitsOnly, input.selection.start)
                val newTextFieldValue = TextFieldValue(
                    text = formattedText,
                    selection = TextRange(newCursorPosition)
                )
                onValueChange(newTextFieldValue)
            }
        },
        label = { Text("CPF") },
        singleLine = true,
        modifier = modifier
    )
}

/**
 * Formata o CPF enquanto preserva a posição do cursor.
 *
 * @param cpf Apenas os dígitos do CPF.
 * @param cursorPos A posição atual do cursor antes da formatação.
 * @return Pair contendo o texto formatado e a nova posição do cursor.
 */
fun formatCpfPreservingCursor(cpf: String, cursorPos: Int): Pair<String, Int> {
    val builder = StringBuilder()
    var newCursorPos = cursorPos

    cpf.forEachIndexed { index, char ->
        if (index == 3 || index == 6) {
            builder.append(".")
            if (index < cursorPos) newCursorPos++
        }
        if (index == 9) {
            builder.append("-")
            if (index < cursorPos) newCursorPos++
        }
        builder.append(char)
    }

    return Pair(builder.toString(), newCursorPos.coerceAtMost(builder.length))
}
