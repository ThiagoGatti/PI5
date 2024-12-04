package br.com.analytics.educa.ui.component

fun formatPhoneNumberPreservingCursor(input: String, cursorPosition: Int): Pair<String, Int> {
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
