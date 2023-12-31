package com.example.capturetheflag.models

data class taskResponseModel(
    val success:Boolean,
    val message:String,
    val tasks:ArrayList<TaskModel>
) {
    data class TaskModel(
        val question_id:Int,

        val event_id:Int,

        val question:String,

        val answer:String,

        val unique_code:String
    )
}