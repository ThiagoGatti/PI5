package br.com.analytics.educa.data.model

import br.com.analytics.educa.data.retrofit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val apiService = RetrofitClient.createService(ApiService::class.java)

fun postForm(username: String, userType: String, formName: String, answers: Map<String, Int>) {

    val responseRequest = ResponseRequest(
        action = "saveAnswers",
        userType = userType,
        formName = formName,
        username = username,
        answers = answers
    )

    apiService.enviarRespostas(responseRequest).enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                println("Respostas salvas com sucesso.")
                println(response.body())
            } else {
                println("Erro ao salvar respostas: ${response.message()}")
                println(responseRequest)
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            println("Falha na conexão: ${t.message}")
        }
    })
}

fun autenticarUsuario(
    username: String,
    password: String,
    onSuccess: (String, String) -> Unit,
    onFailure: (String) -> Unit
) {
    val apiService = RetrofitClient.createService(ApiService::class.java)
    val loginRequest = LoginRequest("login", username, password)

    apiService.autenticarUsuario(loginRequest).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null && loginResponse.success) {
                    onSuccess(loginResponse.nome ?: "Usuário", loginResponse.tipo ?: "Desconhecido")
                } else {
                    onFailure(loginResponse?.message ?: "Erro desconhecido")
                }
            } else {
                onFailure("Erro no servidor: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            onFailure("Falha na conexão: ${t.message}")
        }
    }
    )
}

fun buscarFormsRespondidos(
    userType: String,
    login: String,
    onResult: (Set<String>) -> Unit,
    onError: (String) -> Unit = {}
) {
    apiService.getFormsRespondidos(userType = userType, login = login).enqueue(object : Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            if (response.isSuccessful) {
                val forms = response.body()?.toSet() ?: emptySet()
                onResult(forms)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                onError("Erro na resposta: $errorMessage")
                onResult(emptySet())
            }
        }

        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            val errorMessage = t.localizedMessage ?: "Falha na conexão"
            onError("Falha ao buscar formulários: $errorMessage")
            onResult(emptySet())
        }
    }
    )
}

fun buscarRespostasPorEscola(
    login: String,
    onResult: (List<ResponseBySchool>) -> Unit,
    onError: (String) -> Unit = {}
) {
    apiService.getResponsesBySchool(login = login).enqueue(object : Callback<List<ResponseBySchool>> {
        override fun onResponse(
            call: Call<List<ResponseBySchool>>,
            response: Response<List<ResponseBySchool>>
        ) {
            if (response.isSuccessful) {
                val responses = response.body() ?: emptyList()
                onResult(responses)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                onError("Erro na resposta: $errorMessage")
            }
        }

        override fun onFailure(call: Call<List<ResponseBySchool>>, t: Throwable) {
            val errorMessage = t.localizedMessage ?: "Falha na conexão"
            onError("Falha ao buscar respostas: $errorMessage")
        }
    }
    )
}

fun agruparRespostasPorTipoPessoa(respostas: List<ResponseBySchool>): Map<String, Map<String, Map<String, Float>>> {
    val agrupamento = mutableMapOf<String, MutableMap<String, MutableMap<String, MutableList<Int>>>>()

    respostas.forEach { resposta ->
        val tipoPessoa = resposta.tipo_usuario
        val formulario = resposta.nome_formulario

        if (!agrupamento.containsKey(tipoPessoa)) {
            agrupamento[tipoPessoa] = mutableMapOf()
        }
        if (!agrupamento[tipoPessoa]!!.containsKey(formulario)) {
            agrupamento[tipoPessoa]!![formulario] = mutableMapOf()
        }

        resposta.respostas.forEach { (pergunta, valor) ->
            if (!agrupamento[tipoPessoa]!![formulario]!!.containsKey(pergunta)) {
                agrupamento[tipoPessoa]!![formulario]!![pergunta] = mutableListOf()
            }
            agrupamento[tipoPessoa]!![formulario]!![pergunta]!!.add(valor)
        }
    }

    return agrupamento.mapValues { (_, formularios) ->
        formularios.mapValues { (_, perguntas) ->
            perguntas.mapValues { (_, valores) ->
                valores.average().toFloat()
            }
        }
    }
}

fun calcularMediaGeralPorTipoPessoa(agrupamento: Map<String, Map<String, Map<String, Float>>>): Map<String, Float> {
    val mediasPorTipoPessoa = mutableMapOf<String, Float>()

    agrupamento.forEach { (tipoPessoa, formularios) ->
        val totalNotas = formularios.values.flatMap { it.values }.sum()
        val totalPerguntas = formularios.values.flatMap { it.values }.size
        val media = if (totalPerguntas > 0) totalNotas / totalPerguntas else 0f

        mediasPorTipoPessoa[tipoPessoa] = media
    }

    return mediasPorTipoPessoa
}

fun buscarDadosEscola(
    login: String,
    onResult: (String, Float, Map<String, Float>) -> Unit,
    onError: (String) -> Unit = {}
) {
    apiService.getSchoolPerformance(login = login).enqueue(object : Callback<SchoolPerformanceResponse> {
        override fun onResponse(
            call: Call<SchoolPerformanceResponse>,
            response: Response<SchoolPerformanceResponse>
        ) {
            if (response.isSuccessful) {
                val schoolData = response.body()
                if (schoolData != null) {
                    val nomeEscola = schoolData.nome_escola
                    val mediaNotas = schoolData.media_nota

                    buscarRespostasPorEscola(
                        login = login,
                        onResult = { respostas ->
                            val agrupamento = agruparRespostasPorTipoPessoa(respostas)
                            val mediasPorTipoPessoa = calcularMediaGeralPorTipoPessoa(agrupamento)
                            onResult(nomeEscola.toString(), mediaNotas, mediasPorTipoPessoa)
                        },
                        onError = { error ->
                            onError("Erro ao buscar respostas: $error")
                        }
                    )
                } else {
                    onError("Erro: Resposta inválida do servidor")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                onError("Erro na resposta: $errorMessage")
            }
        }

        override fun onFailure(call: Call<SchoolPerformanceResponse>, t: Throwable) {
            val errorMessage = t.localizedMessage ?: "Falha na conexão"
            onError("Falha ao buscar dados da escola: $errorMessage")
        }
    })
}

fun mediaEscola(mediasPorTipo : Map<String, Float>, mediaNotas : Float): Float {
    var mediaEscola : Float = 0f
    mediasPorTipo.forEach { (_, media) ->
        mediaEscola = media + mediaEscola
    }
    mediaEscola = mediaNotas + mediaEscola
    mediaEscola = mediaEscola / (mediasPorTipo.size + 1)
    mediaEscola = String.format("%.1f", mediaEscola).toFloat()
    return mediaEscola
}

fun buscarBoletim(
    login: String,
    onResult: (List<Boletim>) -> Unit,
    onError: (String) -> Unit = {}
) {
    apiService.getBoletim(login = login).enqueue(object : Callback<List<Boletim>> {
        override fun onResponse(call: Call<List<Boletim>>, response: Response<List<Boletim>>) {
            if (response.isSuccessful) {
                val boletim = response.body() ?: emptyList()
                onResult(boletim)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                onError("Erro na resposta: $errorMessage")
            }
        }

        override fun onFailure(call: Call<List<Boletim>>, t: Throwable) {
            val errorMessage = t.localizedMessage ?: "Falha na conexão"
            onError("Falha ao buscar boletim: $errorMessage")
        }
    })
}

fun fetchUsersByType(type: String, onResult: (List<User>) -> Unit) {
    val call = apiService.getUsersByType(type = type)
    call.enqueue(object : Callback<List<User>> {
        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
            if (response.isSuccessful) {
                onResult(response.body() ?: emptyList())
            } else {
                println("Erro ao buscar usuários: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<List<User>>, t: Throwable) {
            println("Falha ao buscar usuários: ${t.message}")
            onResult(emptyList())
        }
    })
}

suspend fun removeUser(login: String, onComplete: () -> Unit) {
    withContext(Dispatchers.IO) {
        val call = apiService.removeUser(UserRemoveRequest(login = login))
        call.execute()
    }
    withContext(Dispatchers.Main) {
        onComplete()
    }
}

fun fetchTurmas(onResult: (List<String>) -> Unit) {
    val call = apiService.getTurmas()
    call.enqueue(object : Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            if (response.isSuccessful) {
                onResult(response.body() ?: emptyList())
            } else {
                onResult(emptyList())
            }
        }

        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            onResult(emptyList())
        }
    })
}

fun fetchUsersByTurma(turma: String, onResult: (List<User>) -> Unit) {
    val call = apiService.getUsersByTurma(turma = turma)
    call.enqueue(object : Callback<List<User>> {
        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
            if (response.isSuccessful) {
                onResult(response.body() ?: emptyList())
            } else {
                onResult(emptyList())
                println("Erro na resposta: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<List<User>>, t: Throwable) {
            onResult(emptyList())
            println("Falha na conexão: ${t.message}")
        }
    })
}

fun fetchUserCompleto(
    login: String,
    onResult: (UserCompleto) -> Unit,
    onError: (String) -> Unit
) {
    apiService.getUserCompleto(login = login).enqueue(object : Callback<UserCompleto> {
        override fun onResponse(call: Call<UserCompleto>, response: Response<UserCompleto>) {
            if (response.isSuccessful) {
                response.body()?.let { onResult(it) }
            } else {
                onError("Erro ao buscar usuário: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<UserCompleto>, t: Throwable) {
            onError("Falha na conexão: ${t.message}")
        }
    })
}

fun updateUserCompleto(
    user: UserCompleto,
    onComplete: (Boolean) -> Unit
) {
    apiService.editUserCompleto(user).enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            onComplete(response.isSuccessful && response.body()?.success == true)
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            onComplete(false)
        }
    })
}