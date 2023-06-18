package com.example.mafia

object Preferences {
    //Теги для сохранения данных в SharedPreferences
    const val TABLE_NAME = "TABLE"

    const val USERNAME_TAG = "USERNAME"
    //Теги для запуска диалогов
    const val DIALOG_TAG_REGISTER = "REGISTRATION"

    const val DIALOG_TAG_JOIN = "JOIN"
    //Теги для определения параметров доступа
    const val PROPERTY_TAG = "PROPERTY"

    const val HOST_TAG = "HOST"
    const val PLAYER_TAG = "PLAYER"

    //Теги для передачи информации интентами
    const val ROOM_CODE = "CODE"
    const val PLAYER_ID = "ID"
    const val GAME_DATA = "GAME"

    //Переменные для обозначения фазы игры
    enum class GamePhases {
        Meeting,
        Night,
        Day,
        End
    }
}
