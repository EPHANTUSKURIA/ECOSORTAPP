package com.example.ecosortapp

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class BlogsData : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blogs_data)

        // Load pie chart data into WebView
        loadPieChart()
        // Load histogram chart data into WebView
        loadHistogramChart()


        // Initialize print button
        val printButton: Button = findViewById(R.id.printButton)
        printButton.setOnClickListener {
            // Implement printing functionality
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with printing
                createPdf()
            } else {
                // Permission not granted, request permission
               requestStoragePermission()
            }

        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE)
    }

    private fun loadPieChart() {
        val webView: WebView = findViewById(R.id.pieChartWebView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Generate HTML content for the pie chart (you can replace this with your actual data)
        val htmlContent = """
            <html>
<head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Client', 'Points Earned'],
                ['0-50', 25],
                ['51-100', 20],
                ['101-150', 15],
                ['151-200', 10],
                ['201+', 5]
            ]);
            var options = {
                title: 'Distribution of Clients by Points Earned',
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

        webView.loadData(htmlContent, "text/html", null)
    }

    private fun loadHistogramChart() {
        val webView: WebView = findViewById(R.id.histogramChartWebView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Generate HTML content for the histogram chart (you can replace this with your actual data)
        val htmlContent = """
            <html>
<head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Number of Blogs', 'Frequency'],
                ['0-5', 10],
                ['6-10', 15],
                ['11-15', 8],
                ['16-20', 5],
                ['21+', 3]
            ]);
            var options = {
                title: 'Distribution of Blog Contributions',
                legend: { position: 'none' }
            };
            var chart = new google.visualization.BarChart(document.getElementById('histogramchart'));
            chart.draw(data, options);
        }
    </script>
</head>
<body>
    <div id="histogramchart" style="width: 100%; height: 100%;"></div>
</body>
</html>
        """.trimIndent()

        webView.loadData(htmlContent, "text/html", null)
    }

    private fun createPdf() {
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val printAdapter = findViewById<WebView>(R.id.pieChartWebView).createPrintDocumentAdapter("PieChart")
        val printAdapter2 = findViewById<WebView>(R.id.histogramChartWebView).createPrintDocumentAdapter("HistogramChart")

        printManager.print("BlogsData", printAdapter, PrintAttributes.Builder().build())
        printManager.print("BlogsData", printAdapter2, PrintAttributes.Builder().build())
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with printing
                createPdf()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
