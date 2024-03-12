package com.sebesti0n.capturetheflag.apiServices

import com.sebesti0n.capturetheflag.models.EventsSchema
import com.sebesti0n.capturetheflag.models.EventX
import com.sebesti0n.capturetheflag.models.LoginReponse
import com.sebesti0n.capturetheflag.models.NextRiddleModel
import com.sebesti0n.capturetheflag.models.QuestionModel
import com.sebesti0n.capturetheflag.models.RegisterResponse
import com.sebesti0n.capturetheflag.models.ResponseEventModel
import com.sebesti0n.capturetheflag.models.ResponseQuestionModel
import com.sebesti0n.capturetheflag.models.StandingModel
import com.sebesti0n.capturetheflag.models.StatusModel
import com.sebesti0n.capturetheflag.models.TeamSchema
import com.sebesti0n.capturetheflag.models.User
import com.sebesti0n.capturetheflag.models.UserLoginDetails
import com.sebesti0n.capturetheflag.models.taskResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiEndpoints {

    @POST("/register")
    fun register(
        @Body
        users: User?
    ): Call<RegisterResponse>

    @POST("/login")
    fun login(
        @Body
        userCredentials: UserLoginDetails?
    ): Call<LoginReponse>

    @POST("/admin/create")
    fun createEvent(
        @Body
        event: EventX
    ): Call<ResponseEventModel>

    @GET("/admin/event")
    fun getEvent(
        @Query("owner")
        ownerID: Int
    ): Call<ResponseEventModel>

    @GET("/admin/event-details")
    fun getEventbyId(
        @Query("eid")
        eid: Int
    ): Call<ResponseEventModel>

    @GET("/event/event-details")
    fun getEventDetails(
        @Query("enroll_id")
        enrollID: String,
        @Query("eid")
        eid: Int
    ): Call<EventsSchema>

    @GET("/event/register")
    fun getRegisteredEventbyId(
        @Query("enrollid")
        enrollid: String
    ): Call<ResponseEventModel>

    @GET("/event/history")
    fun getPreviousEventbyId(
        @Query("uid")
        uid: Int
    ): Call<ResponseEventModel>

    @GET("/event/live")
    fun getliveEvent(): Call<ResponseEventModel>


    //all events (live + upcoming)
    @GET("/event/all")
    fun getAllEvent(): Call<ResponseEventModel>

    @POST("/admin/addRiddles")
    fun addTasks(
        @Body
        tList: ArrayList<QuestionModel>
    ): Call<taskResponseModel>

    @GET("/event/event-registration")
    fun registerUserForRegistration(
        @Query("uid")
        uid: Int,
        @Query("eid")
        eid: Int
    ): Call<StatusModel>

    @GET("/event/get-riddles")
    fun getRiddles(
        @Query("eid")
        eid: Int,
        @Query("uid")
        uid: Int
    ): Call<ResponseQuestionModel>

    @GET("/event/submit")
    fun getSubmissionDetails(
        @Query("eid")
        eid: Int,
        @Query("uid")
        uid: Int
    ): Call<NextRiddleModel>

    @GET("/event/leaderboard")
    fun getStandings(
        @Query("eid")
        eid: Int
    ): Call<StandingModel>

    @POST("/event/register-team")
    fun registerTeamforEvents(
        @Body
        team: TeamSchema
    ): Call<StatusModel>

}