package br.com.analytics.educa.data.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api.php")
    fun autenticarUsuario(@Body loginData: LoginRequest): Call<LoginResponse>
}

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    val success: Boolean,
    val nome: String? = null,
    val tipo: String? = null,
    val message: String? = null
)