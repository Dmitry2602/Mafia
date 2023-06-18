package com.example.mafia.activities.local_activities

import android.os.Parcel
import android.os.Parcelable

open class GameInfoParcelable(
    var players: ArrayList<PlayerParcelable>?,
    var turn: Int,
    var gamePhase: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(PlayerParcelable),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(players)
        parcel.writeInt(turn)
        parcel.writeString(gamePhase)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameInfoParcelable> {
        override fun createFromParcel(parcel: Parcel): GameInfoParcelable {
            return GameInfoParcelable(parcel)
        }

        override fun newArray(size: Int): Array<GameInfoParcelable?> {
            return arrayOfNulls(size)
        }
    }
}