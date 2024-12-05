package br.com.analytics.educa.ui.screen.users.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SpecificUserFields(
    userType: String,
    onFieldsUpdated: (Map<String, Any>) -> Unit // Callback para atualizar os campos
) {
    Column {
        when (userType) {
            "ALUNO" -> {
                var selectedYear by remember { mutableStateOf("1") }
                var selectedClass by remember { mutableStateOf("A") }
                val combinedValue = selectedYear + selectedClass

                val yearOptions = (1..9).map { it.toString() }
                val classOptions = listOf("A", "B", "C", "D", "E")

                Text("Informações Específicas do Aluno", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                DropdownField(
                    label = "Selecione o Ano",
                    options = yearOptions,
                    selectedOption = selectedYear,
                    onOptionSelected = { year ->
                        selectedYear = year
                        onFieldsUpdated(mapOf("Turma" to combinedValue))
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                DropdownField(
                    label = "Selecione a Turma",
                    options = classOptions,
                    selectedOption = selectedClass,
                    onOptionSelected = { classLetter ->
                        selectedClass = classLetter
                        onFieldsUpdated(mapOf("Turma" to combinedValue))
                    }
                )
            }

            "PROFESSOR" -> {
                var selectedSubject by remember { mutableStateOf("") }
                val subjectOptions = listOf(
                    "Matemática", "Português", "Ciências", "História", "Geografia",
                    "Educação Física", "Inglês", "Espanhol", "Artes"
                )

                val availableClasses = (1..9).flatMap { year ->
                    listOf("A", "B", "C", "D", "E").map { section -> "$year$section" }
                }

                val selectedClasses = remember { mutableStateListOf<String>() }

                Text("Informações Específicas do Professor", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                DropdownField(
                    label = "Selecione a Matéria",
                    options = subjectOptions,
                    selectedOption = selectedSubject,
                    onOptionSelected = {
                        selectedSubject = it
                        onFieldsUpdated(
                            mapOf(
                                "Matéria" to selectedSubject,
                                "Turmas" to selectedClasses.toList()
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MultiSelectDropdown(
                    label = "Turmas",
                    options = availableClasses,
                    selectedOptions = selectedClasses.toList(),
                    onSelectionChange = { updatedClasses ->
                        selectedClasses.clear()
                        selectedClasses.addAll(updatedClasses)
                        onFieldsUpdated(
                            mapOf(
                                "Matéria" to selectedSubject,
                                "Turmas" to selectedClasses.toList()
                            )
                        )
                    }
                )
            }

            "FUNCIONARIO" -> {
                var selectedDepartment by remember { mutableStateOf("") }
                val departmentOptions = listOf("Secretaria", "Manutenção", "TI", "Recepção")

                Text("Informações Específicas do Funcionário", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                DropdownField(
                    label = "Selecione a Função",
                    options = departmentOptions,
                    selectedOption = selectedDepartment,
                    onOptionSelected = {
                        selectedDepartment = it
                        onFieldsUpdated(mapOf("Função" to selectedDepartment))
                    }
                )
            }

            "DIRETOR" -> {
                Text(
                    text = "Diretor não possui campos específicos.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            else -> {
                Text(
                    text = "Tipo de usuário desconhecido.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}

@Composable
fun MultiSelectDropdown(
    label: String,
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChange: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedOptions.joinToString(", "),
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) { // Certifica que o clique alterna o estado
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp) // Define altura máxima
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val updatedSelections = if (selectedOptions.contains(option)) {
                                selectedOptions - option
                            } else {
                                selectedOptions + option
                            }
                            onSelectionChange(updatedSelections)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = selectedOptions.contains(option),
                        onCheckedChange = { isChecked ->
                            val updatedSelections = if (isChecked) {
                                selectedOptions + option
                            } else {
                                selectedOptions - option
                            }
                            onSelectionChange(updatedSelections)
                        }
                    )
                    Text(text = option, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}