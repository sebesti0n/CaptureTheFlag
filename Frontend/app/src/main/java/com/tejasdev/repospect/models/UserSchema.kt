package com.tejasdev.repospect.models

data class UserSchema(
    val user_id:Int,
    val CollegeName: String,
    val email: String,
    val FirstName: String,
    val LastName: String,
    val MobileNo: String,
    val cnfpassword: String,
    val password: String,
    val enroll_id:String
)
