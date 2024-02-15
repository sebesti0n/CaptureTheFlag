package com.example.capturetheflag.models

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
    val unique_code: String
)