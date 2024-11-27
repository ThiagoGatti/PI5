package br.com.analytics.educa.ui.route

object Route {

    // TELAS INICIAIS
    const val initialScreen = "initialScreen" // tela inicial
    const val login = "login" // tela login
    const val userVerification = "userVerification" // verificacao usuario

    // TELAS ALUNOS

    const val menuAluno = "menuAluno" // tela menu aluno
        const val menuAlunoForm = "menuAlunoForm"
            const val formAlunoAutonomia = "formAlunoAutonomia"
            const val formAlunoClimaEscolar = "formAlunoClima"
            const val formAlunoQualidadeEnsino = "formAlunoQualidade"
            const val formAlunoInfraestrutura = "formAlunoInfraestrutura"
            const val formAlunoGestao = "formAlunoGestao"
            const val formAlunoConsideracoesFinais = "formAlunoConsideracoes"

    // TELAS PROFESSORES

    const val menuProfessores = "menuProfessores"
        const val menuProfessoresForm = "menuProfessoresForm"
            const val formProfessoresClima = "formProfessoresClima"
            const val formProfessoresQualidade = "formProfessoresQualidade"
            const val formProfessoresCondicao = "formProfessoresCondicao"
            const val formProfessoresParticipacao = "formProfessoresParticipacao"
            const val formProfessoresDesafios = "formProfessoresDesafios"

    // TELAS FUNCIONARIOS

    const val menuFuncionarios = "menuFuncionarios"
        const val menuFuncionariosForm = "menuFuncionariosForm"
            const val formFuncionariosDesafios = "formFuncionariosDesafios"
            const val formFuncionariosEficiencia = "formFuncionariosEficiencia"
            const val formFuncionariosInfraestrutura = "formFuncionariosInfraestrutura"
            const val formFuncionariosSatisfacao = "formFuncionariosSatisfacao"

    // TELAS DIRETORES

    const val menuDiretores = "menuDiretores"

}
