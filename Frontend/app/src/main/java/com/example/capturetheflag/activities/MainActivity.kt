package com.example.capturetheflag.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.ActivityMainBinding
import com.example.capturetheflag.session.Session


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref =Session(this)
        if (sharedPref.isLogin()){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
        else {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.login)
        }


    }

}