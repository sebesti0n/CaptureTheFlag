package com.example.capturetheflag.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    fun hideNavigation(){
        binding.bottomNav.visibility = View.GONE
    }
    fun showNavigation(){
        binding.bottomNav.visibility = View.VISIBLE
    }

}

