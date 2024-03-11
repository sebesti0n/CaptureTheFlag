package com.sebesti0n.capturetheflag.models

data class ResponseQuestionModel(
    val success: String,
    val message:String,
    val riddles:ArrayList<QuestionModel>
)
