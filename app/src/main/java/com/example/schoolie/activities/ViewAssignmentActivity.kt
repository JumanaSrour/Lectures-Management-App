package com.example.schoolie.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolie.R
import kotlinx.android.synthetic.main.activity_lecture_details.view.*
import kotlinx.android.synthetic.main.activity_view_assignment.*
import okio.ByteString.Companion.encode
import java.net.URLEncoder

class ViewAssignmentActivity : AppCompatActivity() {

    var assignment = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_assignment)

        assignment = intent.getStringExtra("assignment").toString()
        pdfOpen(assignment)
    }

    private fun pdfOpen(fileUrl: String) {

        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true


        var url = ""
        val pdf = fileUrl

        try {
            url = URLEncoder.encode(pdf, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$url")

    }

    private class Callback : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}
