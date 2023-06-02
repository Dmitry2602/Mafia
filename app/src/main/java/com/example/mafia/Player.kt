package com.example.mafia

data class Player(
    var playerId: String,
    val nickname: String,
    val role: Role,
    val isAlive: Boolean,
    val votedAgainst: String
) {
    constructor() : this("", "", Role.Unknown, true, "")

    enum class Role {
        Mafia,
        Civilian,
        Sheriff,
        Doctor,
        Unknown
    }
}
