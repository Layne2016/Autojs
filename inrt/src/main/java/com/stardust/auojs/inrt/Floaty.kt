package com.stardust.auojs.inrt

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnInvokeView
import com.stardust.auojs.inrt.autojs.AutoJs
import com.stardust.auojs.inrt.auto_web.AutoWebView
import com.stardust.auojs.inrt.widget.ScaleImage
import com.stardust.autojs.core.console.ConsoleImpl
import com.stardust.autojs.core.console.ConsoleView



object Floaty {

    @SuppressLint("ClickableViewAccessibility")
    fun showAppFloat(activity: Activity, tag: String) {
        EasyFloat.with(activity)
            .setTag(tag)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setSidePattern(SidePattern.RESULT_SIDE)
            .setLocation(100, 100)
            .setAppFloatAnimator(null)
            .setLayout(R.layout.float_app, OnInvokeView {
                val content = it.findViewById<RelativeLayout>(R.id.rlContent)
                val params = content.layoutParams as FrameLayout.LayoutParams
                it.findViewById<ScaleImage>(R.id.ivScale).onScaledListener =
                    object : ScaleImage.OnScaledListener {
                        override fun onScaled(x: Float, y: Float, event: MotionEvent) {
                            params.width = max(params.width + x.toInt(), 100)
                            params.height = max(params.height + y.toInt(), 100)
                            content.layoutParams = params
                        }
                    }
                it.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                    EasyFloat.dismissAppFloat(tag)
                }
                val webView = it.findViewById<AutoWebView>(R.id.floatyWeb)
                webView.apply {
                    loadUrl("http://127.0.0.1:8080")
                }
                val consoleView = it.findViewById<ConsoleView>(R.id.console)

                it.findViewById<RadioGroup>(R.id.rgTab).setOnCheckedChangeListener { radioGroup, i ->
                    when (i) {
                        R.id.rbUI ->{
                            webView.visibility= View.VISIBLE
                            consoleView.visibility= View.GONE
                        }
                        R.id.rbLog ->{
                            webView.visibility= View.GONE
                            consoleView.visibility= View.VISIBLE
                        }
                    }
                }
                consoleView.apply {
                    setConsole(AutoJs.instance.globalConsole as ConsoleImpl)
                    logListRecyclerView.apply {
                        setOnTouchListener { _, event ->
                            EasyFloat.appFloatDragEnable(event?.action == MotionEvent.ACTION_UP)
                            false
                        }
                    }
                }.hideInput()
            })
            .show()
    }

    private fun max(x: Int, y: Int): Int {
        return if (x > y) x else y
    }

}