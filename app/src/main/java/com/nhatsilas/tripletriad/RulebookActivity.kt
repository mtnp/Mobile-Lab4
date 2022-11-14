package com.nhatsilas.tripletriad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class RulebookActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rulebook)

        webView = findViewById(R.id.webview);
        webView.loadUrl("https://finalfantasy.fandom.com/wiki/Triple_Triad_(Final_Fantasy_VIII)")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }
}