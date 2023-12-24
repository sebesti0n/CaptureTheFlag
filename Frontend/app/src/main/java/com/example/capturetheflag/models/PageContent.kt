package com.example.capturetheflag.models

import android.os.Parcel
import android.os.Parcelable

data class PageContent(val imageResId: Int, val text: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageResId)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PageContent> {
        override fun createFromParcel(parcel: Parcel): PageContent {
            return PageContent(parcel)
        }

        override fun newArray(size: Int): Array<PageContent?> {
            return arrayOfNulls(size)
        }
    }
}

