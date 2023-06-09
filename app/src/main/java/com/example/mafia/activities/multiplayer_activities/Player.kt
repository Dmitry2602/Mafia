package com.example.mafia.activities.multiplayer_activities

data class Player(
    var playerId: String,
    val username: String,
    var role: String,
    val isAlive: Boolean,
    val votedAgainst: String
) {
    constructor() : this("", "", Role.Unknown.toString(), true, "")

    enum class Role {
        Mafia,
        Civilian,
        Sheriff,
        Doctor,
        Unknown
    }
}






