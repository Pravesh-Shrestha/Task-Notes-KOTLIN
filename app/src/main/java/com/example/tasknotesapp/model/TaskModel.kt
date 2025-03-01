package com.example.tasknotesapp.model

import android.os.Parcel
import android.os.Parcelable

data class TaskModel(
    var taskId: String = "",
    var title: String = "",
    var description: String = "",
    var isCompleted: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() == 1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(if (isCompleted) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<TaskModel> {
        override fun createFromParcel(parcel: Parcel): TaskModel = TaskModel(parcel)
        override fun newArray(size: Int): Array<TaskModel?> = arrayOfNulls(size)
    }
}