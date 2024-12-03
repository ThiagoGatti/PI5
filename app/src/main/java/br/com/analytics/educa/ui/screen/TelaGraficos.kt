package br.com.analytics.educa.ui.screen

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.analytics.educa.data.retrofit.ApiService
import br.com.analytics.educa.data.retrofit.RetrofitClient
import br.com.analytics.educa.data.retrofit.SchoolPerformance
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TelaGraficos(username, navigateBack: () -> Unit) {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(androidx.compose.ui.graphics.Color(0xFF551BA8), androidx.compose.ui.graphics.Color(0xFF9752E7))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Desempenho Escolar",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )

            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                Text(text = "Erro: $errorMessage", color = androidx.compose.ui.graphics.Color.Red)
            } else if (schoolPerformance.isEmpty()) {
                Text(text = "Carregando dados...", color = androidx.compose.ui.graphics.Color.Gray)
            } else {
                AndroidView(
                    factory = { ctx -> createBarChart(ctx, schoolPerformance) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF5D145B)),
                modifier = Modifier
                    .width(150.dp)
                    .height(45.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Ícone de voltar",
                        tint = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voltar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        }
    }
}

fun createBarChart(context: Context, data: List<SchoolPerformance>): BarChart {
    val barChart = BarChart(context)

    val entriesNotas = data.mapIndexed { index, performance ->
        BarEntry(index.toFloat(), performance.media_nota)
    }

    val entriesPresenca = data.mapIndexed { index, performance ->
        BarEntry(index.toFloat(), performance.media_presenca)
    }

    val dataSetNotas = BarDataSet(entriesNotas, "Média de Notas").apply {
        color = Color.GREEN
    }

    val dataSetPresenca = BarDataSet(entriesPresenca, "Média de Presença (%)").apply {
        color = Color.BLUE
    }

    barChart.data = BarData(dataSetNotas, dataSetPresenca)
    barChart.description.text = "Desempenho Escolar"
    barChart.xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return data.getOrNull(value.toInt())?.materia ?: ""
            }
        }
    }
    barChart.animateY(1000)
    barChart.invalidate()

    return barChart
}