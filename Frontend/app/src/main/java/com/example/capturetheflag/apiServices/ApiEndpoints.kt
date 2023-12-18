package com.example.capturetheflag.apiServices

import com.example.capturetheflag.models.LoginReponse
import com.example.capturetheflag.models.User
import com.example.capturetheflag.models.UserLoginDetails
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiEndpoints {
    @POST("/register")
    fun register(
        @Body
        users:User?
    ) : Call<JSONObject>

    @POST("/login")
    fun login(
        @Body
        userCredentials:UserLoginDetails?
    ) : Call<LoginReponse>

}