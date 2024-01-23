package com.example.capturetheflag.ui

import android.app.Application
import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.RegisterResponse
import com.example.capturetheflag.models.User
import com.example.capturetheflag.sharedprefrences.userPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    private val app:Application
) : AndroidViewModel(app) {
private var registerResposeLiveData= MutableLiveData<RegisterResponse>()
    fun get():LiveData<RegisterResponse>?{
        return registerResposeLiveData!!
    }
    private val session = userPreferences.getInstance(app.applicationContext)

    fun register(user:User){
            RetrofitInstances.service.register(user).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    registerResposeLiveData.value = response.body()
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }

            })
    }
}