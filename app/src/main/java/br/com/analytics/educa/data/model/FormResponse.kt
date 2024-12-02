package br.com.analytics.educa.data.model

import br.com.analytics.educa.data.retrofit.ApiResponse
import br.com.analytics.educa.data.retrofit.ApiService
import br.com.analytics.educa.data.retrofit.ResponseRequest
import br.com.analytics.educa.data.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun postForm(username: String, userType: String, formName: String, answers: Map<String, Int>) {
    val apiService = RetrofitClient.createService(ApiService::class.java)

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

fun buscarFormsRespondidos(
    userType: String,
    login: String,
    onResult: (Set<String>) -> Unit,
    onError: (String) -> Unit = {}
) {
    val apiService = RetrofitClient.createService(ApiService::class.java)
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
    })
}