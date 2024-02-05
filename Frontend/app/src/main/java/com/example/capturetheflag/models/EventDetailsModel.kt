package com.example.capturetheflag.models

data class EventDetailsModel(
    val event: ArrayList<Event>,
    val message: String,
    val success: Boolean,
    val isRegister:Boolean
)
