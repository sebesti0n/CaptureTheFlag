package com.sebesti0n.capturetheflag.models

data class LoginReponse(
    val success:Boolean,
    val message:String,
    val userDetails:UserSchema
)
