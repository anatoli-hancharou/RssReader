package com.ppo.rssreader.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.ppo.rssreader.R
import kotlinx.android.synthetic.main.webview.*
import java.net.MalformedURLException
import java.net.URL


class WebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("url")

        setContentView(R.layout.webview)

        val mWebView = findViewById<WebView>(R.id.webview)

        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.cacheMode = LOAD_CACHE_ELSE_NETWORK
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.setAppCachePath("/data/$packageName/cache");
        mWebView.loadUrl(url!!)
        mWebView.webViewClient = WebViewClient()

        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, newUrl: String): Boolean {
                var isLocalUrl = false
                try {
                    val givenUrl = URL(newUrl)
                    val host: String = givenUrl.host
                    if (host.contains(URL(url).host)) isLocalUrl = true
                } catch (e: MalformedURLException) { }

                return if (isLocalUrl) false else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newUrl))
                    startActivity(intent)
                    true
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}