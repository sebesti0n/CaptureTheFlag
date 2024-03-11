package com.sebesti0n.capturetheflag.models

data class StandingModel(
    val success:Boolean,
    val message:String,
    val list:ArrayList<LeaderBoardModel>
)