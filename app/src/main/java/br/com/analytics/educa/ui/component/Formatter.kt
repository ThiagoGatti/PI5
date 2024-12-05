package br.com.analytics.educa.ui.component

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun entradaTextoCPF(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val digitsOnly = input.text.filter { it.isDigit() } // Remove caracteres não numéricos
            if (digitsOnly.length <= 11) { // Limita a entrada a 11 dígitos
                val (formattedText, newCursorPosition) = formaterCPF(digitsOnly, input.selection.start)
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
fun formaterCPF(cpf: String, cursorPos: Int): Pair<String, Int> {
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


/**
 * Formata e valida uma data no formato DD/MM/YYYY.
 *
 * @param input A string de entrada que representa a data.
 * @param cursorPosition A posição atual do cursor.
 * @return Um par com a string formatada e a nova posição do cursor, ou null se a data for inválida.
 */
fun formaterValidarDataCursor(input: String, cursorPosition: Int): Pair<String, Int> {
    val digitsOnly = input.filter { it.isDigit() } // Remove caracteres não numéricos
        .take(8) // Limita os números a no máximo 8 dígitos (DDMMYYYY sem barras)

    val formatted = StringBuilder()
    var adjustedCursor = cursorPosition

    digitsOnly.forEachIndexed { index, char ->
        if (index == 2 || index == 4) {
            formatted.append("/")
            if (index < cursorPosition) adjustedCursor++
        }
        formatted.append(char)
    }

    val result = formatted.toString()
    return result.take(10) to adjustedCursor.coerceAtMost(result.length) // Limita a 10 caracteres
}



/**
 * Verifica se uma data no formato DD/MM/YYYY é válida.
 *
 * @param date A string da data no formato DD/MM/YYYY.
 * @return True se a data for válida, False caso contrário.
 */
/**
 * Verifica se uma data no formato DD/MM/YYYY é válida.
 *
 * @param date A string da data no formato DD/MM/YYYY.
 * @return True se a data for válida, False caso contrário.
 */
fun validarData(date: String): Boolean {
    if (date.length != 10) return false // Garantir que o comprimento seja DD/MM/YYYY

    val parts = date.split("/")
    if (parts.size != 3) return false // Garantir que a data tenha dia, mês e ano

    val day = parts[0].toIntOrNull() ?: return false
    val month = parts[1].toIntOrNull() ?: return false
    val year = parts[2].toIntOrNull() ?: return false

    // Verificar limites de mês e ano
    if (month !in 1..12) return false
    if (year !in 1900..Calendar.getInstance().get(Calendar.YEAR)) return false

    // Verificar limites de dia baseado no mês e ano
    val daysInMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> return false
    }

    return day in 1..daysInMonth
}

/**
 * Verifica se um ano é bissexto.
 *
 * @param year O ano a ser verificado.
 * @return True se o ano for bissexto, False caso contrário.
 */
fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}


/**
 * Permite entrada gradual de data no formato DD/MM/YYYY.
 *
 * @param input A string de entrada.
 * @return True se a entrada for válida até o momento, False caso contrário.
 */
fun isValidDateInput(input: String): Boolean {
    val regex = Regex("^\\d{0,2}/?\\d{0,2}/?\\d{0,4}\$")
    return regex.matches(input)
}

fun phoneNumberFormatter(input: String, cursorPosition: Int): Pair<String, Int> {
    val digitsOnly = input.filter { it.isDigit() }.take(11) // Limita a 11 dígitos
    val formatted = StringBuilder()
    var adjustedCursor = cursorPosition

    digitsOnly.forEachIndexed { index, char ->
        when (index) {
            0 -> {
                formatted.append("(")
                if (index < cursorPosition) adjustedCursor++
            }
            2 -> {
                formatted.append(") ")
                if (index < cursorPosition) adjustedCursor += 2
            }
            7 -> {
                formatted.append("-")
                if (index < cursorPosition) adjustedCursor++
            }
        }
        formatted.append(char)
    }

    return formatted.toString() to adjustedCursor.coerceAtMost(formatted.length)
}

fun formatDateFromDatabase(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: date)
    } catch (e: Exception) {
        date
    }
}

fun formatDateToDatabase(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: date)
    } catch (e: Exception) {
        date
    }
}