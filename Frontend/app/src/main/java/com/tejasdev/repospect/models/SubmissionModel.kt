package com.tejasdev.repospect.models


data class SubmissionModel(
    val eid:Int,
    val tid:Int,
    val currRid:Int,
    val nextRid:Int,
    val unqCode:String,
    val answer:String
)
