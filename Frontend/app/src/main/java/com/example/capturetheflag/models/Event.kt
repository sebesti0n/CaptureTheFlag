package com.example.capturetheflag.models

import java.sql.Timestamp

data class Event(
    val event_id:Int,
    val title:String,
    val location:String,
    val description:String,
    val owner_id:Int,
    val start_time:Timestamp,
    val end_time:Timestamp,
    val No_of_questions:Int,
    val organisation:String
)
