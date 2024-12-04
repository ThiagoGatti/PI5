package br.com.analytics.educa.ui.component.usecase

class ValidarEnviarNota {
    fun validarNota(nota: String): Double? {
        val notaDouble = nota.toDoubleOrNull()
        return if (notaDouble != null && notaDouble in 0.0..10.0) notaDouble else null
    }


    fun validarFaltas(faltas: String): Int? {

        val totalAulas = 20
        val faltasInt = faltas.toIntOrNull()

        if(faltasInt != null && faltasInt in 0..totalAulas){

            val aulasAssistidas = totalAulas - faltasInt
            val porcentagemPresenca = (aulasAssistidas * 100) / totalAulas

            return porcentagemPresenca
        }
        else {
            return null
        }

    }
}