package com.together.togetherproject.presentation.post.view

import android.app.Dialog
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.together.togetherproject.R

class AddressSearchBottomSheet : BottomSheetDialogFragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_address_search, container, false)
        webView = view.findViewById(R.id.webView)
        setupWebView()
        return view
    }

    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val newWebView = WebView(requireContext())
                newWebView.settings.javaScriptEnabled = true
                newWebView.settings.domStorageEnabled = true

                val dialog = Dialog(requireContext())
                dialog.setContentView(newWebView)
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                dialog.show()

                newWebView.webChromeClient = this
                newWebView.webViewClient = WebViewClient()

                val transport = resultMsg?.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()

                return true
            }
        }

        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(AndroidBridge(), "Android")
        webView.loadUrl("https://togetherproject-fd339.web.app/daum_address.html")
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(zoneCode: String, roadAddress: String) {
            Log.d("AddressBridge", "zoneCode: $zoneCode, roadAddress: $roadAddress")
            activity?.runOnUiThread {
                (activity as? PostWriteActivity)?.setAddress(roadAddress)
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(): AddressSearchBottomSheet {
            return AddressSearchBottomSheet()
        }
    }
}