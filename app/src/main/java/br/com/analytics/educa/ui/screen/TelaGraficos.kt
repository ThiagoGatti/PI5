package br.com.analytics.educa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.analytics.educa.data.retrofit.ApiService
import br.com.analytics.educa.data.retrofit.RetrofitClient
import br.com.analytics.educa.data.retrofit.SchoolPerformance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TelaGraficos(navigateBack: () -> Unit) {
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
                    colors = listOf(Color(0xFF551BA8), Color(0xFF9752E7)) // Gradiente roxo
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Permite rolar conteúdo longo
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título da tela
            Text(
                text = "Desempenho Escolar",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )

            // Exibir mensagens de erro ou carregamento
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                Text(text = "Erro: $errorMessage", color = Color.Red)
            } else if (schoolPerformance.isEmpty()) {
                Text(text = "Carregando dados...", color = Color.Gray)
            } else {
                RenderBarChart(data = schoolPerformance)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Botão de voltar no rodapé
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Alinha o botão no rodapé
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D145B)),
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
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voltar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
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
                    color = Color(0xFFFF0032),
                    topLeft = androidx.compose.ui.geometry.Offset(barX, (size.height - barHeightNota).toFloat()),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeightNota.toFloat())
                )

                // Presença Bar
                drawRect(
                    color = Color(0xFF444329),
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
