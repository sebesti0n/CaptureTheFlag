package com.example.capturetheflag.models


data class Event(
    val event_id:Int,
    val title: String,
    val location:String,
    val description:String,
    val owner_id:Int,
    val start_time:String,
    val end_time:String,
    val No_of_questions:Int,
    val organisation:String,
    val posterImage:String
)
