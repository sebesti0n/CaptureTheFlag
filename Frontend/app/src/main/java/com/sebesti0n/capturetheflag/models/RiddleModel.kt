package com.sebesti0n.capturetheflag.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "riddle_table")
data class RiddleModel(
    val answer: String,
    val event_id: Int,
    val level: Int,
    val question: String,
    @PrimaryKey
    val question_id: Int,
    val storyline: String,
    val unique_code: String,
    val Hint1:String,
    val Hint2:String,
    val Hint3:String,
    val imageLink:String,
    val riddleImageLink:String,
    val Latitude:Double,
    val Longitude:Double,
    val Range:Int
)