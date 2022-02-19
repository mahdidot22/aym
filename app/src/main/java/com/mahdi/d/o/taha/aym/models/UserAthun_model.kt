package com.mahdi.d.o.taha.aym.models

import android.os.Parcel
import android.os.Parcelable

data class UserAthun_model(val id: String, val username: String, val psw: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeString(psw)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAthun_model> {
        override fun createFromParcel(parcel: Parcel): UserAthun_model {
            return UserAthun_model(parcel)
        }

        override fun newArray(size: Int): Array<UserAthun_model?> {
            return arrayOfNulls(size)
        }

        val COL_USER_ID = "id"
        val COL_USER_NAME = "username"
        val COL_USER_PASSWOR = "password"

        val TABLE_NAME = "users"
        val TABLE_CREATE_USERS =
            "create table $TABLE_NAME" +
                "($COL_USER_ID integer primary key autoincrement," +
                "$COL_USER_NAME text not null," +
                "$COL_USER_PASSWOR text not null)"
    }
}
