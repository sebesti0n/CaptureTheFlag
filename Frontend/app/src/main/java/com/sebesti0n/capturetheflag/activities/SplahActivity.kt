package com.sebesti0n.capturetheflag.activities

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.sebesti0n.capturetheflag.databinding.ActivitySplahBinding
import com.sebesti0n.capturetheflag.R

class SplahActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplahBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = getColor(R.color.blue_darkest)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        window.statusBarColor = getColor(R.color.blue_darkest)
        val x= WindowCompat.getInsetsController(window,window.decorView)
        x.systemBarsBehavior= WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        val head=binding.splash1
        val logo=binding.logo
        head.alpha=0f
        logo.alpha=0f
        logo.animate().setDuration(900).alpha(1f)
        typerfunc()
        head.animate().setDuration(901).alpha(1f).withEndAction {
                val i = Intent(this, MainActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                startActivity(i)
                finish()
    }
    }
    private fun typerfunc() {
        val typer=findViewById<TextView>(R.id.splash1)
        val label = "Dream Code Conquer"
        val stringbuilder = StringBuilder()

        Thread{
            for(letter in label){
                stringbuilder.append(letter)
                Thread.sleep(50)
                runOnUiThread{
                    typer.text= stringbuilder.toString()
                }
            }
        }.start()
    }

}