package com.sebesti0n.capturetheflag.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sebesti0n.capturetheflag.R
import com.sebesti0n.capturetheflag.apiServices.BackupInstances
import com.sebesti0n.capturetheflag.databinding.ActivityMainBinding
import com.sebesti0n.capturetheflag.models.StatusModel
import com.sebesti0n.capturetheflag.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: Session
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.statusBarColor = resources.getColor(R.color.blue_darkest)

//        isAllOkay()
        sharedPref =Session(this)
        if (sharedPref.isLogin()){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            navController.navigate(R.id.login)
//            setupActionBarWithNavController(navController)
        }


    }

    private fun isAllOkay() {
        val call = BackupInstances.service
        call.allOkay().enqueue(object: Callback<StatusModel> {
            override fun onResponse(call: Call<StatusModel>, response: Response<StatusModel>) {
                if(response.isSuccessful){
                    val isOkay = response.body()?.success
                    if(isOkay == false)
                        moveToBackupPlan()
                }
            }

            override fun onFailure(call: Call<StatusModel>, t: Throwable) {
                Log.e("sebastian MainActivity","backup Server Fail - ${t.message}")
            }

        })
    }

    private fun moveToBackupPlan() {
        val intent = Intent(this,WebViewActivity::class.java)
        startActivity(intent)
        finish()
    }

}