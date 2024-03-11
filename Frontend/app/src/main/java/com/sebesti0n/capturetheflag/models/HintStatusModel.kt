package com.sebesti0n.capturetheflag.models

data class HintStatusModel(
    val hint1: Boolean,
    val hint2: Boolean,
    val hint3: Boolean,
    val message: String,
    val success: Boolean,
    val unlockedIn: Long
)