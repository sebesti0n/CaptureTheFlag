package com.example.capturetheflag.models

data class ResponseEventModel(
    val event: ArrayList<Event>,
    val message: String,
    val status:Int,
    val success: Boolean
)