package com.example.capturetheflag.models

data class EventX(
    val No_of_questions: Int,
    val description: String,
    val end_time: String,
    val organisation: String,
    val location: String,
    val owner_id: Int,
    val posterImage: String,
    val start_time: String,
    val title: String
)