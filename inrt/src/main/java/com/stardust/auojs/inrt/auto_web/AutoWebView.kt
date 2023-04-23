package com.stardust.auojs.inrt.auto_web

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient


class AutoWebView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {


    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun init() {
        val settings = settings
        settings.useWideViewPort = true
        settings.builtInZoomControls = true
        settings.loadWithOverviewMode = true
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.domStorageEnabled = true
        settings.displayZoomControls = false
        addJavascriptInterface(AutoWebBridge.instance().apply {
            autoWebView = this@AutoWebView
        }, "bridge")
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //todo 注入autojs 方法给vue调用
            }
        }
        webChromeClient = WebChromeClient()
    }

    fun callJS(jsCode: String) {
        loadUrl("javascript:$jsCode")
    }


    init {
        init()
    }


}