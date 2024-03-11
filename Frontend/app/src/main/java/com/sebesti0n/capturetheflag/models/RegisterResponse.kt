package com.sebesti0n.capturetheflag.models

class RegisterResponse(
    val success :Boolean,
    val message: String,
    val user: UserSchema
)