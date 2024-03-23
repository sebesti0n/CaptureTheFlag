package com.tejasdev.repospect.models

import com.google.gson.annotations.SerializedName

data class CtfState(
    val message: String,
    val next: Next,
    @SerializedName("rList")
    val riddleModelList: List<RiddleModel>,
    val success: Boolean
)