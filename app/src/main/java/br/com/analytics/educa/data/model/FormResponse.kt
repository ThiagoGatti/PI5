package br.com.analytics.educa.data.model

import br.com.analytics.educa.data.retrofit.ApiResponse
import br.com.analytics.educa.data.retrofit.ApiService
import br.com.analytics.educa.data.retrofit.ResponseRequest
import br.com.analytics.educa.data.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun FormResponse(username: String, userType: String, formName: String, answers: Map<String, Int>) {
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


