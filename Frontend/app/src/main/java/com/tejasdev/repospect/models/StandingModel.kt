package com.tejasdev.repospect.models

data class StandingModel(
    val success:Boolean,
    val message:String,
    val list:ArrayList<LeaderBoardModel>
)