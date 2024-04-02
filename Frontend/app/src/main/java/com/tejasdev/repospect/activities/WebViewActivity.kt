package com.tejasdev.repospect.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tejasdev.repospect.R
import com.tejasdev.repospect.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val url = intent.getStringExtra("link")
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.statusBarColor = resources.getColor(R.color.gray)
        if (url != null){
            binding.webview.loadUrl(url)
        }
        binding.webview.settings.javaScriptEnabled = true;
    }
}