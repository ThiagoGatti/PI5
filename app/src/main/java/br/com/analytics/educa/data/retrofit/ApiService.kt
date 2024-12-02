package br.com.analytics.educa.data.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("api.php")
    fun getFormsRespondidos(
        @Query("action") action: String = "getAnsweredForms",
        @Query("userType") userType: String,
        @Query("login") login: String
    ): Call<List<String>>

    @POST("api.php")
    fun autenticarUsuario(@Body loginData: LoginRequest): Call<LoginResponse>

    @POST("api.php")
    fun enviarRespostas(@Body responseRequest: ResponseRequest): Call<ApiResponse>

    @GET("api.php")
    fun getSchoolPerformance(
        @Query("action") action: String = "schoolPerformance"
    ): Call<List<SchoolPerformance>>
}

data class LoginRequest(
    val action: String = "login",
    val username: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val nome: String? = null,
    val tipo: String? = null,
    val message: String? = null
)

data class ResponseRequest(
    val action: String = "saveAnswers",
    val userType: String,
    val formName: String,
    val username: String,
    val answers: Map<String, Int>
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)

data class SchoolPerformance(
    val materia: String,
    val media_nota: Double,
    val media_presenca: Double
)