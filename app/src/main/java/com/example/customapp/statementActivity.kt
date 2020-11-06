package com.example.customapp

import android.graphics.Color
import android.media.audiofx.AudioEffect
import android.os.Bundle
import android.os.DropBoxManager
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class statementActivity: AppCompatActivity() {
    //val simpleDateFormat = SimpleDateFormat("mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statments_layout)

        var graph= findViewById<LineChart>(R.id.graph)
        var iGraph = findViewById<LineChart>(R.id.investedGraph)
        if(inventory.cBalance.isNotEmpty()) {
            var data = LineDataSet(getDataPoint(),"Data Set 1")

            val dataSet = ArrayList<ILineDataSet>()
            dataSet.add(data)
            val linedata = LineData(dataSet)
            data = myDataSettings(data)
            graph.data = linedata
            graph = myGraphSettings(graph)
            graph.invalidate()

            val xAxis = graph.xAxis
            xAxis.valueFormatter = MyBalanceAxisValueFormatter()
        }
        if(inventory.moneyInvested.isNotEmpty()){
            var iData = LineDataSet(getDataPointInvested(),"Data Set 2")
            val iDataSet = ArrayList<ILineDataSet>()
            iDataSet.add(iData)
            val ilinedata = LineData(iDataSet)
            iData = myDataSettings(iData)
            iGraph.data = ilinedata
            iGraph = myGraphSettings(iGraph)
            iGraph.invalidate()

            val xxAxis = iGraph.xAxis
            xxAxis.valueFormatter = MyInvestedAxisValueFormatter()
        }

    }

    private fun  getDataPoint(): ArrayList<Entry> {
        val dataValue = ArrayList<Entry>()

        for ((x, i) in inventory.cBalance.withIndex()) {
                    dataValue.add(Entry(x + 1.toFloat(), i))
        }
        return dataValue
    }
    private fun getDataPointInvested(): ArrayList<Entry> {
        val dataValue = ArrayList<Entry>()

        for ((x, i) in inventory.moneyInvested.withIndex()) {

            dataValue.add(Entry(x + 1.toFloat(), i))
        }

        return dataValue
    }
    private fun myGraphSettings(lineChart: LineChart): LineChart {
        lineChart.setDrawBorders(true)
        lineChart.setDrawGridBackground(true)
        lineChart.setBorderColor(resources.getColor(R.color.dGreen))
        lineChart.setGridBackgroundColor(resources.getColor(R.color.dGreen))
        val description = Description()
        description.text = ""
        lineChart.description = description
        return lineChart
    }
    private fun myDataSettings( lineDataSet:LineDataSet): LineDataSet {
        lineDataSet.valueFormatter = myValueFormatter()
        lineDataSet.lineWidth = 4.0f
        lineDataSet.color = resources.getColor(R.color.green)
        lineDataSet.setCircleColor(resources.getColor(R.color.gWhite))
        lineDataSet.circleRadius = 5.0f
        lineDataSet.valueTextSize = 14.0f
        lineDataSet.valueTextColor = resources.getColor(R.color.gWhite)
        return lineDataSet
    }
    class myValueFormatter:IValueFormatter{
        override fun getFormattedValue(
            value: Float,
            entry: Entry?,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler?
        ): String {
            return "$ " + value
        }

    }
    class MyBalanceAxisValueFormatter: IAxisValueFormatter{
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            axis?.labelCount = inventory.cBalance.size
            return value.toString()
        }

    }
    class MyInvestedAxisValueFormatter: IAxisValueFormatter{
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            axis?.labelCount = inventory.moneyInvested.size
            return value.toString()
        }

    }
}