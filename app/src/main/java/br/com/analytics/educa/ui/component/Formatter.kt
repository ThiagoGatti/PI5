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
            val digitsOnly = input.text.filter { it.isDigit() }
            if (digitsOnly.length <= 11) {
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


fun formaterValidarDataCursor(input: String, cursorPosition: Int): Pair<String, Int> {
    val digitsOnly = input.filter { it.isDigit() }
        .take(8)

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
    return result.take(10) to adjustedCursor.coerceAtMost(result.length)
}



fun validarData(date: String): Boolean {
    if (date.length != 10) return false

    val parts = date.split("/")
    if (parts.size != 3) return false

    val day = parts[0].toIntOrNull() ?: return false
    val month = parts[1].toIntOrNull() ?: return false
    val year = parts[2].toIntOrNull() ?: return false

    // Verificar limites de mês e ano
    if (month !in 1..12) return false
    if (year !in 1900..Calendar.getInstance().get(Calendar.YEAR)) return false


    val daysInMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> return false
    }

    return day in 1..daysInMonth
}


fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

fun isValidDateInput(input: String): Boolean {
    val regex = Regex("^\\d{0,2}/?\\d{0,2}/?\\d{0,4}\$")
    return regex.matches(input)
}

fun phoneNumberFormatter(input: String, cursorPosition: Int): Pair<String, Int> {
    val digitsOnly = input.filter { it.isDigit() }.take(11)
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