package com.example.capturetheflag.models

data class Next(
    val Number_correct_answer: Int,
    val Rank: Int,
    val end_time: String,
    val event_id: Int,
    val is_registered: Boolean,
    val participation_id: Int,
    val registration_time: String,
    val sequence: List<Int>,
    val start_time: String,
    val team_id: Int
)