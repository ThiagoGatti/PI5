package br.com.analytics.educa.domain.usecase

class EnviarNotaUseCase {
    fun validarNota(nota: String): Double? {
        val notaDouble = nota.toDoubleOrNull()
        return if (notaDouble != null && notaDouble in 0.0..10.0) notaDouble else null
    }

    fun validarFaltas(faltas: String): Int? {

        //aulas totais - falta

        // -> procentagem

        return faltas.toIntOrNull()

    }
}
