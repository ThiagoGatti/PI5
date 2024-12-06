package br.com.analytics.educa.ui.component.usecase

fun validarFaltas(faltas: String): Int? {

    val totalAulas = 20
    val faltasInt = faltas.toIntOrNull()

    if (faltasInt != null && faltasInt in 0..totalAulas) {

        val aulasAssistidas = totalAulas - faltasInt
        val porcentagemPresenca = (aulasAssistidas * 100) / totalAulas

        return porcentagemPresenca
    } else {
        return null
    }

}