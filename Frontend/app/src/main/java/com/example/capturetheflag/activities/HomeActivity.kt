package com.example.capturetheflag.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
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
import androidx.navigation.fragment.NavHostFragment
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarHome.toolbar)

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
                    navController.navigate(R.id.createEventFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
                    true
                }
                    R.id.nav_home->{
                    navController.navigate(R.id.homefragment)
                        drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
                        true
        }
                R.id.nav_registeredHunt->{
                    navController.navigate(R.id.registerHuntFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
                    true
                }
                R.id.nav_history->{
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


    //Bottom Navigation item seclection handle

        binding.appBarHome.navBar.setOnItemSelectedListener{
            when(it.itemId){
            R.id.home->{
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home) as NavHostFragment
                navController = navHostFragment.navController
                navController.navigate(R.id.homefragment)
                true
            }
            R.id.history->{
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home) as NavHostFragment
                navController = navHostFragment.navController
                navController.navigate(R.id.historyHuntFragment)
                true
            }
            R.id.reg_hunt->{
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home) as NavHostFragment
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

    //Header Click handle

        val headerView = navView.getHeaderView(0)
        val headerImage :ImageView = headerView.findViewById(R.id.headerProfilePic)
        val uName :TextView = headerView.findViewById(R.id.headerUsername)
        val uOrganisation :TextView = headerView.findViewById(R.id.headerOrganisation)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}