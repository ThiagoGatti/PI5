package br.com.analytics.educa.ui.component

import android.graphics.Color
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color.Companion.White

@Composable
fun GraficoBarraRespostasPorFormulario(
    agrupamento: Map<String, Map<String, Float>>,
    formularioSelecionado: String
) {
    val respostas = agrupamento[formularioSelecionado] ?: emptyMap()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Formulário: $formularioSelecionado",
            style = MaterialTheme.typography.headlineMedium,
            color = White, // Corrigido
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        600
                    )
                    val entries = respostas.entries.mapIndexed { index, (pergunta, media) ->
                        BarEntry(index.toFloat(), media)
                    }

                    val dataSet = BarDataSet(entries, "Médias por Pergunta").apply {
                        setColors(*ColorTemplate.COLORFUL_COLORS)
                        valueTextColor = AndroidColor.WHITE
                        valueTextSize = 12f
                    }

                    val barData = BarData(dataSet)
                    this.data = barData

                    this.xAxis.apply {
                        valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(
                            respostas.keys.toList()
                        )
                        position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        textColor = AndroidColor.WHITE
                    }

                    this.axisLeft.textColor = AndroidColor.WHITE
                    this.axisRight.isEnabled = false
                    this.description.text = ""
                    this.legend.textColor = AndroidColor.WHITE
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
        )
    }
}

@Composable
fun GraficoPizza(
    dados: Map<String, Float>,
    titulo: String
) {
    val total = dados.values.sum()
    val porcentagens = dados.mapValues { (it.value / total) * 100 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = White, // Texto em branco para contraste
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AndroidView(
            factory = { context ->
                com.github.mikephil.charting.charts.PieChart(context).apply {
                    val entries = porcentagens.map { (tipoPessoa, porcentagem) ->
                        PieEntry(porcentagem, tipoPessoa)
                    }
                    val dataSet = PieDataSet(entries, "").apply {
                        colors = listOf(
                            android.graphics.Color.parseColor("#FFBB86FC"),
                            android.graphics.Color.parseColor("#FF03DAC5"),
                            android.graphics.Color.parseColor("#FF6200EE"),
                            android.graphics.Color.parseColor("#FF3700B3")
                        ) // Cores personalizadas que combinam com o tema
                        valueTextColor = android.graphics.Color.WHITE // Valores em branco
                        valueTextSize = 12f
                    }
                    this.data = PieData(dataSet).apply {
                        setValueTextColor(android.graphics.Color.WHITE) // Texto em branco
                        setValueTextSize(14f)
                    }
                    this.setEntryLabelColor(android.graphics.Color.WHITE) // Rótulos em branco
                    this.setEntryLabelTextSize(12f)
                    this.description.isEnabled = false // Remove descrição padrão
                    this.legend.apply {
                        textColor = android.graphics.Color.WHITE // Legenda em branco
                        textSize = 12f
                        isEnabled = true // Habilita legenda
                    }
                    this.setUsePercentValues(true) // Exibe valores como porcentagem
                    this.invalidate()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}