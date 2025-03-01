package com.example.tasknotesapp.model

import android.os.Parcel
import android.os.Parcelable

data class NoteModel(
    var id: String = "",
    var title: String = "",
    var content: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(content)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<NoteModel> {
        override fun createFromParcel(parcel: Parcel): NoteModel = NoteModel(parcel)
        override fun newArray(size: Int): Array<NoteModel?> = arrayOfNulls(size)
    }
}