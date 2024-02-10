package com.example.capturetheflag.activities

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.ActivityHomeBinding
import com.example.capturetheflag.sharedprefrences.userPreferences

class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding
    private lateinit var session:userPreferences
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupDrawerLayout()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home) as NavHostFragment
        navController=navHostFragment.navController
        session = userPreferences(this)
        binding.llForCreate.setOnClickListener {
            openAdminActivity()
        }
        binding.llForHome.setOnClickListener {
            openHome()
        }
        binding.llForRegisterHunt.setOnClickListener {
            openRegisterHunt()
        }
        binding.llForHistory.setOnClickListener {
            openHistorty()
        }
        binding.llForLogout.setOnClickListener {
            logOut()
        }
        }

    private fun logOut() {
        session.logOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun openHistorty() {
        navController.navigate(R.id.historyHuntFragment)
    }

    private fun openRegisterHunt() {
        navController.navigate(R.id.registerHuntFragment)
    }

    private fun openHome() {
        navController.navigate(R.id.homefragment)
    }

    private fun openAdminActivity() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    private fun setupDrawerLayout() {
       val gestureDetector = GestureDetector(this, object: GestureDetector.SimpleOnGestureListener(){
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                    if((e1?.x ?: 0f) < (e2.x)){
                        openDrawerLayout()
                        return true
                    }
                    else if((e1?.x?:0f) > e2.x){
                        closeDrawerLayout()
                        return true
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
        }
       )
    }
    private fun openDrawerLayout(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeDrawerLayout(){
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }
}

