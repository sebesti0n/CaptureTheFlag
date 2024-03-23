package com.tejasdev.repospect.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.tejasdev.repospect.apiServices.RetrofitInstances
import com.tejasdev.repospect.models.RegisterResponse
import com.tejasdev.repospect.models.User
import com.tejasdev.repospect.models.UserSchema
import com.tejasdev.repospect.session.Session

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