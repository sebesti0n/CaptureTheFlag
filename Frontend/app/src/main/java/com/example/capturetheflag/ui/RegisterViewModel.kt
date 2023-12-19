package com.example.capturetheflag.ui

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capturetheflag.Repository.RegisterRepository
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.RegisterResponse
import com.example.capturetheflag.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
private var registerResposeLiveData= MutableLiveData<RegisterResponse>()
    fun get():LiveData<RegisterResponse>?{
        return registerResposeLiveData!!
    }


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