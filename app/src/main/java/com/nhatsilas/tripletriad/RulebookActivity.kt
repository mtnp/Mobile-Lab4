package com.nhatsilas.tripletriad

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class RulebookActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rulebook)

        webView = findViewById(R.id.webview)
        // enables the back button for google docs
        webView.setWebViewClient(WebViewClient())
        //wiki
//        webView.loadUrl("https://finalfantasy.fandom.com/wiki/Triple_Triad_(Final_Fantasy_VIII)")
        // english rulebook
        webView.loadUrl("https://docs.google.com/document/d/1rCAHPoe0bDjFoa5NWxic3xiV9qJ2aimFN2oLq-j_Zrg/edit")
        // spanish rulebook
//        webView.loadUrl("https://docs.google.com/document/d/1joMy9QJT27csr6W5uy527W_ECv242J3b/edit")
        //blog
//        webView.loadUrl("https://medium.com/@silasp0801/cs371p-fall-2022-minh-triet-pham-47886f0ccac6")

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