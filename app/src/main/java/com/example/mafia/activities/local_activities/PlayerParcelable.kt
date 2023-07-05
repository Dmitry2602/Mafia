package com.example.mafia.activities.local_activities

import android.os.Parcel
import android.os.Parcelable

open class PlayerParcelable(
    var id: Int,
    val username: String?,
    var role: String?,
    var wasKilled: Boolean,
    var wasKicked: Boolean,
    var gotVotes: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    enum class Role {
        Mafia,
        Civilian
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(username)
        parcel.writeString(role)
        parcel.writeByte(if (wasKilled) 1 else 0)
        parcel.writeByte(if (wasKicked) 1 else 0)
        parcel.writeInt(gotVotes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayerParcelable> {
        override fun createFromParcel(parcel: Parcel): PlayerParcelable {
            return PlayerParcelable(parcel)
        }

        override fun newArray(size: Int): Array<PlayerParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
