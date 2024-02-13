package com.example.capturetheflag.models

data class RiddleModel(
    val answer: String,
    val event_id: Int,
    val level: Int,
    val question: String,
    val question_id: Int,
    val storyline: String,
    val unique_code: String
)