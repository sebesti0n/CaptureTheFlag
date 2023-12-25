package com.example.capturetheflag.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.ActivityMainBinding
import com.example.capturetheflag.sharedprefrences.userPreferences


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPref: userPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref =userPreferences(this)
//        if (!sharedPref.getUserFirstTime()){
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            navController.navigate(R.id.homefragment)
//        }
//        else {
//            val navHostFragment =
//                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//            navController = navHostFragment.navController
//            navController.navigate(R.id.login)
//        }
        binding.navBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->{
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    navController = navHostFragment.navController
                    navController.navigate(R.id.homefragment)
                    true
                }
                R.id.history->{
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    navController = navHostFragment.navController
                    navController.navigate(R.id.historyHuntFragment)
                    true
                }
                R.id.reg_hunt->{
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    navController = navHostFragment.navController
                    navController.navigate(R.id.registerHuntFragment)
                    true
                }

                else -> {
                    Log.w("sebastian","else")
                    true
                }
            }
        }

    }

}