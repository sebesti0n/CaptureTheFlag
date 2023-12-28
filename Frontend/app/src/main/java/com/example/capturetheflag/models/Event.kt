package com.example.capturetheflag.models

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp

data class Event(
    val event_id:Int,
    val title:String,
    val location:String,
    val description:String,
    val owner_id:Int,
    val start_time:Timestamp,
    val end_time:Timestamp,
    val No_of_questions:Int,
    val organisation:String,
    val posterImage:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readSerializable() as Timestamp,
        parcel.readSerializable() as Timestamp,
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(event_id)
        parcel.writeString(title)
        parcel.writeString(location)
        parcel.writeString(description)
        parcel.writeInt(owner_id)
        parcel.writeSerializable(start_time)
        parcel.writeSerializable(end_time)
        parcel.writeInt(No_of_questions)
        parcel.writeString(organisation)
        parcel.writeString(posterImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}
