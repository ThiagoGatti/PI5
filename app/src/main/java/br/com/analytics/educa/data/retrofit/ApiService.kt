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

    @POST("api.php")
    fun editUser(@Body user: UserEditRequest): Call<ApiResponse>

    @POST("api.php")
    fun removeUser(@Body user: UserRemoveRequest): Call<ApiResponse>

    @GET("api.php")
    fun getResponsesBySchool(
        @Query("action") action: String = "getResponsesBySchool",
        @Query("login") login: String
    ): Call<List<ResponseBySchool>>

    @GET("api.php")
    fun getSchoolPerformance(
        @Query("action") action: String = "schoolPerformance",
        @Query("login") login: String
    ): Call<SchoolPerformanceResponse>

    @GET("api.php")
    fun getBoletim(
        @Query("action") action: String = "getBoletim",
        @Query("login") login: String
    ): Call<List<Boletim>>

    @GET("api.php")
    fun getUsersByType(
        @Query("action") action: String = "getUsersByType",
        @Query("type") type: String
    ): Call<List<User>>

    @GET("api.php")
    fun getTurmas(
        @Query("action") action: String = "getTurmas"
    ): Call<List<String>>

    @GET("api.php")
    fun getUsersByTurma(
        @Query("action") action: String = "getUsersByTurma",
        @Query("turma") turma: String
    ): Call<List<User>>

    @POST("api.php")
    fun editUserCompleto(@Body user: UserCompleto): Call<ApiResponse>

    @GET("api.php")
    fun getUserCompleto(
        @Query("action") action: String = "getUserDetails",
        @Query("login") login: String
    ): Call<UserCompleto>
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

data class ResponseBySchool(
    val tipo_usuario: String,
    val nome_formulario: String,
    val respostas: Map<String, Int>
)

data class SchoolPerformanceResponse(
    val nome_escola: String?,
    val media_nota: Float
)

data class Boletim(
    val materia: String,
    val nota: Float,
    val presenca: Int
)

data class User(
    val login: String,
    val name: String,
    val phone: String,
    val type: String
)

data class UserEditRequest(
    val action: String = "editUser",
    val login: String,
    val name: String,
    val phone: String
)

data class UserRemoveRequest(
    val action: String = "removeUser",
    val login: String
)

data class UserCompleto(
    val login: String,
    val name: String,
    val cpf: String,
    val birthDate: String,
    val phone: String,
    val type: String,
    val password: String?,
    val components: Map<String, Any> = emptyMap()
)