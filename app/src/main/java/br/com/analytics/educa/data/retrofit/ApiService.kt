package br.com.analytics.educa.data.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api.php")
    fun autenticarUsuario(@Body loginData: LoginRequest): Call<LoginRequest>
}

data class LoginRequest(val username: String, val password: String)