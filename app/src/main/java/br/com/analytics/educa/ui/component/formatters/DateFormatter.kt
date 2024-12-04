import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Formata e valida uma data no formato DD/MM/YYYY.
 *
 * @param input A string de entrada que representa a data.
 * @param cursorPosition A posição atual do cursor.
 * @return Um par com a string formatada e a nova posição do cursor, ou null se a data for inválida.
 */
fun formatAndValidateDatePreservingCursor(input: String, cursorPosition: Int): Pair<String, Int> {
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
fun isValidDate(date: String): Boolean {
    if (date.length != 10) return false // Garantir que o comprimento seja DD/MM/YYYY

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dateFormatter.isLenient = false

    return try {
        dateFormatter.parse(date) != null
    } catch (e: Exception) {
        false
    }
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
