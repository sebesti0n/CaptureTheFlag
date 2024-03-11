package com.sebesti0n.capturetheflag.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Event(
    val No_of_questions: Int,
    val description: String,
    val end_ms: String,
    val end_time: String,
    val event_id: Int,
    val event_type: Int,
    val levels: Int,
    val location: String,
    val node_at_each_level: Int,
    val organisation: String,
    val owner_id: Int,
    val posterImage: String,
    val start_ms: String,
    val start_time: String,
    val title: String
): Parcelable

