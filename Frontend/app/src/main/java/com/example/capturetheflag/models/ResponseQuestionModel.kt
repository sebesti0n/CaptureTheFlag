package com.example.capturetheflag.models

data class ResponseQuestionModel(
    val success: String,
    val message:String,
    val questions:ArrayList<QuestionModel>
)
