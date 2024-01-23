package com.example.capturetheflag.models

data class LoginReponse(
    val success:Boolean,
    val message:String,
    val userDetails:UserSchema
)
