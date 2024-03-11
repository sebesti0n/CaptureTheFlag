package com.sebesti0n.capturetheflag.models

data class ResponseEventModel(
    val event: ArrayList<Event>,
    val message: String,
    val success: Boolean
)