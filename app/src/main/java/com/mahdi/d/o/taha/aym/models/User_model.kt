package com.mahdi.d.o.taha.aym.models

import android.os.Parcel
import android.os.Parcelable

data class User_model(val id: String, val username: String, val isActive: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeString(isActive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User_model> {
        override fun createFromParcel(parcel: Parcel): User_model {
            return User_model(parcel)
        }

        override fun newArray(size: Int): Array<User_model?> {
            return arrayOfNulls(size)
        }
    }
}
