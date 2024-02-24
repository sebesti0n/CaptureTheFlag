package com.example.capturetheflag.apiServices

import com.example.capturetheflag.models.CtfState
import com.example.capturetheflag.models.HintStatusModel
import com.example.capturetheflag.models.NextRiddleModel
import com.example.capturetheflag.models.SubmissionModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CtfApiEndpoints {

    @GET("/event/start-contest")
    fun getCtfState(
        @Query("eid")
        eid: Int,
        @Query("rid")
        rid: String,
        @Query("st")
        startMs: Long
    ): Call<CtfState>

    @GET("/event/submission")
    fun submitRiddle(
        @Query("eid")
        eid: Int,
        @Query("tid")
        tid: Int,
        @Query("et")
        startMs: Long
    ): Call<NextRiddleModel>
    @POST("/event/riddle-Submission")
    fun submissionRiddle(
        @Body
        submissionModel: SubmissionModel
    ): Call<NextRiddleModel>

    @GET("/event/hintStatus")
    fun getHintStatus(
        @Query("eventId")
        eventId:Int,
        @Query("teamId")
        teamId:Int,
        @Query("currentRiddleId")
        riddleId:Int,
        @Query("hintType")
        hintType:Int
    ):Call<HintStatusModel>
}