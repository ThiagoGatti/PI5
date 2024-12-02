package br.com.analytics.educa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.analytics.educa.data.retrofit.ApiService
import br.com.analytics.educa.data.retrofit.RetrofitClient
import br.com.analytics.educa.data.retrofit.SchoolPerformance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TelaGraficos() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var schoolPerformance by remember { mutableStateOf<List<SchoolPerformance>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Buscar dados da API ao carregar a tela
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val apiService = RetrofitClient.createService(ApiService::class.java)
            apiService.getSchoolPerformance().enqueue(object : Callback<List<SchoolPerformance>> {
                override fun onResponse(
                    call: Call<List<SchoolPerformance>>,
                    response: Response<List<SchoolPerformance>>
                ) {
                    if (response.isSuccessful) {
                        schoolPerformance = response.body() ?: emptyList()
                    } else {
                        errorMessage = "Erro ao carregar os dados: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<List<SchoolPerformance>>, t: Throwable) {
                    errorMessage = "Falha na conexão: ${t.localizedMessage}"
                }
            })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            Text(text = "Erro: $errorMessage", color = Color.Red)
        } else if (schoolPerformance.isEmpty()) {
            Text(text = "Carregando dados...", color = Color.Gray)
        } else {
            Text(
                text = "Desempenho Escolar",
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Black
            )

            RenderBarChart(data = schoolPerformance)
        }
    }
}

@Composable
fun RenderBarChart(data: List<SchoolPerformance>) {
    val barWidth = 60f
    val barSpacing = 40f
    val maxBarHeight = 300f
    val maxNota = data.maxOf { it.media_nota }
    val maxPresenca = data.maxOf { it.media_presenca }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)) {
        translate(left = barSpacing / 2) {
            data.forEachIndexed { index, performance ->
                val barHeightNota = (performance.media_nota / maxNota) * maxBarHeight
                val barHeightPresenca = (performance.media_presenca / maxPresenca) * maxBarHeight

                val barX = index * (barWidth + barSpacing)

                // Nota Bar
                drawRect(
                    color = Color(0xFF6200EA),
                    topLeft = androidx.compose.ui.geometry.Offset(barX, (size.height - barHeightNota).toFloat()),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeightNota.toFloat())
                )

                // Presença Bar
                drawRect(
                    color = Color(0xFF03DAC5),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        barX + barWidth + 10, // Espaço entre barras
                        (size.height - barHeightPresenca).toFloat()
                    ),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeightPresenca.toFloat())
                )
            }
        }
    }
}