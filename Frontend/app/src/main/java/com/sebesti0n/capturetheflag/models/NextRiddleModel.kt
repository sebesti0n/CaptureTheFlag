package com.sebesti0n.capturetheflag.models

data class NextRiddleModel(
    val message: String,
    val next: Int,
    val success: Boolean
)