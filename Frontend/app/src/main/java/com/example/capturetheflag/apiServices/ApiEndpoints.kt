package com.example.capturetheflag.apiServices

import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.models.LoginReponse
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.models.RegisterResponse
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.models.User
import com.example.capturetheflag.models.UserLoginDetails
import com.example.capturetheflag.models.taskResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiEndpoints {
    @POST("/register")
    suspend fun register(
        @Body
        users:User?
    ) : Call<RegisterResponse>

    @POST("/login")
    suspend fun login(
        @Body
        userCredentials:UserLoginDetails?
    ) : Call<LoginReponse>
    @POST("/admin/create")
    suspend fun createEvent(
        @Body
        event: EventX
    ): Call<ResponseEventModel>
    @GET("/admin/event")
    suspend fun getEvent(
        @Query("owner")
        ownerID:Int
    ): Call<ResponseEventModel>
    @GET("/admin/SingleEvent")
    suspend fun getEventbyId(
        @Query("eid")
        eid:Int
    ): Call<ResponseEventModel>
    @GET("/event/register")
    suspend fun getRegisteredEventbyId(
        @Query("uid")
        uid:Int
    ): Call<ResponseEventModel>
    @GET("/event/history")
    suspend fun getPreviousEventbyId(
        @Query("uid")
        uid:Int
    ): Call<ResponseEventModel>
    @GET("/event/live")
    suspend fun getliveEvent(): Call<ResponseEventModel>

    @GET("/event/upcoming")
    suspend fun getupcomingEvent(): Call<ResponseEventModel>

    @POST("/admin/addTask")
    suspend fun addTasks(
        @Body
        tList:ArrayList<QuestionModel>
    ): Call<taskResponseModel>


}