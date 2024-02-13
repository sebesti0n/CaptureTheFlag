package com.example.capturetheflag.models

data class CtfState(
    val message: String,
    val next: Next,
    val riddleModelList: List<RiddleModel>,
    val success: Boolean
)