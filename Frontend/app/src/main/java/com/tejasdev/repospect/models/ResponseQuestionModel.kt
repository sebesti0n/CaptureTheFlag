package com.tejasdev.repospect.models

data class ResponseQuestionModel(
    val success: String,
    val message:String,
    val riddles:ArrayList<QuestionModel>
)
