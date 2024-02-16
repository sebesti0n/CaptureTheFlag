package com.example.capturetheflag.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.RegisterResponse
import com.example.capturetheflag.models.User
import com.example.capturetheflag.models.UserSchema
import com.example.capturetheflag.session.Session
import com.example.capturetheflag.util.Resource

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    private val app:Application
) : AndroidViewModel(app) {

    private val session = Session.getInstance(app.applicationContext)

    fun register(user:User,callback:(Boolean,String?,UserSchema?)->Unit){
            RetrofitInstances.service.register(user).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let{
                            callback(true,it.message,it.user)
                        }
                    }
                    else callback(false,"Something Went Wrong. Try Again!!",null)
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("sebastion Register", t.message.toString())
                    callback(false,"Internal Server Error!",null)
                }

            })
    }
}