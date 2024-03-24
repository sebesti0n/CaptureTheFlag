package com.tejasdev.repospect.models

class RegisterResponse(
    val success :Boolean,
    val message: String,
    val user: UserSchema
)