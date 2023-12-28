package com.example.capturetheflag.models

data class ResponseEventModel(
    val event: List<Event>,
    val message: String,
    val success: Boolean
)