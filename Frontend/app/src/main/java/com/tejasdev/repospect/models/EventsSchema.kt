package com.tejasdev.repospect.models

data class EventsSchema(
    val event: ArrayList<Event>,
    val message: String,
    val isRegister:Boolean,
    val success: Boolean
)