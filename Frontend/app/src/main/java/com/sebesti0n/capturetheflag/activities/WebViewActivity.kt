package com.sebesti0n.capturetheflag.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sebesti0n.capturetheflag.R
import com.sebesti0n.capturetheflag.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWebViewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.statusBarColor = resources.getColor(R.color.gray)
        binding.webview.loadUrl("https://github.com/sebesti0n");
        binding.webview.settings.javaScriptEnabled = true;
    }
}