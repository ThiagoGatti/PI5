package br.com.analytics.educa.ui.component.design

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
import android.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

@Composable
fun GraficoBarraMediaFormularios(
    medias: Map<String, Float>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Médias por Formulário",
            style = MaterialTheme.typography.headlineMedium,
            color = White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    val entries = medias.entries.mapIndexed { index, (formulario, media) ->
                        BarEntry(index.toFloat(), String.format("%.1f", media).toFloat())
                    }

                    val dataSet = BarDataSet(entries, "Médias").apply {
                        setColors(*ColorTemplate.COLORFUL_COLORS)
                        valueTextColor = Color.WHITE
                        valueTextSize = 12f
                        valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return String.format("%.1f", value)
                            }
                        }
                    }

                    this.data = BarData(dataSet)

                    this.xAxis.apply {
                        valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(
                            medias.keys.toList()
                        )
                        position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        textColor = Color.WHITE
                    }

                    this.axisLeft.textColor = Color.WHITE
                    this.axisRight.isEnabled = false
                    this.description.text = ""
                    this.legend.textColor = Color.WHITE
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
            color = White,
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
                        )
                        valueTextColor = Color.WHITE
                        valueTextSize = 12f
                    }
                    this.data = PieData(dataSet).apply {
                        setValueTextColor(android.graphics.Color.WHITE)
                        setValueTextSize(14f)
                    }
                    this.setEntryLabelColor(android.graphics.Color.WHITE)
                    this.setEntryLabelTextSize(12f)
                    this.description.isEnabled = false
                    this.legend.apply {
                        textColor = android.graphics.Color.WHITE
                        textSize = 12f
                        isEnabled = true
                    }
                    this.setUsePercentValues(true)
                    this.invalidate()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}