package com.tejasdev.repospect.models

data class HintStatusModel(
    val hint1: Boolean,
    val hint2: Boolean,
    val hint3: Boolean,
    val message: String,
    val success: Boolean,
    val unlockedIn: Long
)