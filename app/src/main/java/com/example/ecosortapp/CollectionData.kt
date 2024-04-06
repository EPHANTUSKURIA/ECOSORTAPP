package com.example.ecosortapp

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CollectionData : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_data)

        // Generate HTML string for the pie chart
        val pieChartHtml = generatePieChartHtml()
        // Generate HTML string for the histogram chart
        val histogramChartHtml = generateHistogramChartHtml()

        // Load the HTML strings into the WebViews
        val pieChartWebView: WebView = findViewById(R.id.pieChartWebView)
        pieChartWebView.settings.javaScriptEnabled = true
        pieChartWebView.webViewClient = WebViewClient()
        pieChartWebView.loadData(pieChartHtml, "text/html", "UTF-8")

        val histogramChartWebView: WebView = findViewById(R.id.histogramChartWebView)
        histogramChartWebView.settings.javaScriptEnabled = true
        histogramChartWebView.webViewClient = WebViewClient()
        histogramChartWebView.loadData(histogramChartHtml, "text/html", "UTF-8")

        val printButton: Button = findViewById(R.id.printButton)
        printButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                createPdf()
            } else {
                requestStoragePermission()
            }
        }
    }

    private fun generatePieChartHtml(): String {
        // Here you should generate the HTML string for the pie chart
        // You can use JavaScript to draw the pie chart dynamically
        // Example:
        val html = """
            <html>
            <head>
                <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                <script type="text/javascript">
                    google.charts.load('current', {'packages':['corechart']});
                    google.charts.setOnLoadCallback(drawChart);
                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                            ['Task', 'Hours per Day'],
                            ['Work',     11],
                            ['Eat',      2],
                            ['Commute',  2],
                            ['Watch TV', 2],
                            ['Sleep',    7]
                        ]);
                        var options = {
                            title: 'My Daily Activities'
                        };
                        var chart = new google.visualization.PieChart(document.getElementById('piechart'));
                        chart.draw(data, options);
                    }
                </script>
            </head>
            <body>
                <div id="piechart" style="width: 100%; height: 100%;"></div>
            </body>
            </html>
        """.trimIndent()

        return html
    }

    private fun generateHistogramChartHtml(): String {
        // Here you should generate the HTML string for the histogram chart
        // You can use JavaScript to draw the histogram chart dynamically
        // Example:
        val html = """
            <html>
            <head>
                <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                <script type="text/javascript">
                    google.charts.load('current', {'packages':['corechart']});
                    google.charts.setOnLoadCallback(drawChart);
                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                            ['Year', 'Sales', 'Expenses'],
                            ['2004',  1000,      400],
                            ['2005',  1170,      460],
                            ['2006',  660,       1120],
                            ['2007',  1030,      540]
                        ]);
                        var options = {
                            title: 'Company Performance',
                            curveType: 'function',
                            legend: { position: 'bottom' }
                        };
                        var chart = new google.visualization.LineChart(document.getElementById('histogramchart'));
                        chart.draw(data, options);
                    }
                </script>
            </head>
            <body>
                <div id="histogramchart" style="width: 100%; height: 100%;"></div>
            </body>
            </html>
        """.trimIndent()

        return html
    }

    private fun createPdf() {
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val printAdapter = findViewById<WebView>(R.id.pieChartWebView)?.createPrintDocumentAdapter()
        val printAdapter2 = findViewById<WebView>(R.id.histogramChartWebView)?.createPrintDocumentAdapter()

        printAdapter?.let {
            printManager.print("CollectionData - Pie Chart", it, PrintAttributes.Builder().build())
        }

        printAdapter2?.let {
            printManager.print("CollectionData - Histogram Chart", it, PrintAttributes.Builder().build())
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPdf()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
