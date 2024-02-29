package com.example.capturetheflag.models

data class NextRiddleModel(
    val message: String,
    val next: NextRiddLeCountModel,
    val success: Boolean
)