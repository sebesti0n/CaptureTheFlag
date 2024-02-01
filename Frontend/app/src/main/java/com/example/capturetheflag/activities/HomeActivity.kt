package com.example.capturetheflag.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(){
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //Navigation Drawer Layout setup handle

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_home)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.createEventFragment,
                R.id.homefragment, R.id.registerHuntFragment, R.id.historyHuntFragment, R.id.login
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_create->{
                    val intent = Intent(this,AdminActivity::class.java)
                    startActivity(intent)
                    true
                }
                    R.id.nav_home->{

                    navController.navigate(R.id.homefragment)
                        drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
                        true
        }
                R.id.nav_registeredHunt->{
//                    binding.appBarHome.toolbar.title= "Registered Event"
                    navController.navigate(R.id.registerHuntFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
                    true
                }
                R.id.nav_history->{
//                    binding.appBarHome.toolbar.title= "History"
                    navController.navigate(R.id.historyHuntFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
                    true
                }
                else ->{
                    drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
                    false
                }
        }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}