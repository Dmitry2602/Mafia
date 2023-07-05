package com.example.mafia.activities.local_activities

open class GameInfo(
    var players: ArrayList<PlayerParcelable>,
    var turn: Int,
    var gamePhase: String
) {
    //Переменные для обозначения фазы игры
    enum class GamePhases {
        Meeting,
        Night,
        Day,
        MafiaResult,
        GeneralResult
    }
}