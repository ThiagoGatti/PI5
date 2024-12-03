package br.com.analytics.educa.ui.component

import android.graphics.Color
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun GraficoBarraRespostas(agrupamento: Map<String, Map<String, Float>>) {
    agrupamento.forEach { (formulario, respostas) ->
        Text(
            text = "Formulário: $formulario",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
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
                        color = Color.BLUE
                        valueTextColor = Color.BLACK
                        valueTextSize = 12f
                    }

                    val barData = BarData(dataSet)
                    this.data = barData

                    this.xAxis.valueFormatter = IndexAxisValueFormatter(respostas.keys.toList())
                    this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    this.xAxis.granularity = 1f
                    this.xAxis.isGranularityEnabled = true

                    this.axisLeft.axisMinimum = 0f
                    this.axisRight.isEnabled = false
                    this.description.text = ""
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
fun GraficoLinhaTendencias(agrupamento: Map<String, Map<String, Float>>, pergunta: String) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    600
                )

                val entries = agrupamento.entries.mapIndexed { index, (formulario, respostas) ->
                    val media = respostas[pergunta] ?: 0f
                    Entry(index.toFloat(), media)
                }

                val dataSet = LineDataSet(entries, "Tendência para $pergunta").apply {
                    color = Color.GREEN
                    valueTextColor = Color.BLACK
                    valueTextSize = 12f
                    lineWidth = 2f
                }

                val lineData = LineData(dataSet)
                this.data = lineData

                this.xAxis.valueFormatter = IndexAxisValueFormatter(agrupamento.keys.toList())
                this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                this.xAxis.granularity = 1f
                this.xAxis.isGranularityEnabled = true

                this.axisLeft.axisMinimum = 0f
                this.axisRight.isEnabled = false
                this.description.text = ""
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
    )
}