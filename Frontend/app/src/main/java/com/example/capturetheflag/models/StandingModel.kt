package com.example.capturetheflag.models

import java.lang.reflect.Array

data class StandingModel(
    val success:Boolean,
    val message:String,
    val list:ArrayList<LeaderBoardModel>
)