package com.tejasdev.repospect.models

data class ResponseEventModel(
    val event: ArrayList<Event>,
    val message: String,
    val success: Boolean
)