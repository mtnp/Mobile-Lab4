package com.nhatsilas.tripletriad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView

class RulebookActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rulebook)

        webView = findViewById(R.id.webview);
        webView.loadUrl("https://finalfantasy.fandom.com/wiki/Triple_Triad_(Final_Fantasy_VIII)")

        var backBtn : ImageView = findViewById(R.id.backBtn)
        backBtn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

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