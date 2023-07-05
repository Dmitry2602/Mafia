package com.example.mafia

import androidx.core.content.edit
import com.example.mafia.activities.common_activities.preferences
import com.example.mafia.activities.local_activities.GameInfo
import com.google.gson.Gson

object Preferences {
    //Теги для сохранения данных в SharedPreferences
    const val TABLE_NAME = "TABLE"

    const val USERNAME_TAG = "USERNAME"
    const val GAME_DATA = "DATA"

    //Теги для запуска диалогов
    const val DIALOG_TAG_REGISTER = "REGISTRATION"

    const val DIALOG_TAG_JOIN = "JOIN"
    //Теги для определения параметров доступа
    const val PROPERTY_TAG = "PROPERTY"

    const val HOST_TAG = "HOST"
    const val PLAYER_TAG = "PLAYER"

    //Теги для передачи информации в Intent
    const val ROOM_CODE = "CODE"
    const val PLAYER_DATA = "PLAYER"
    const val WINNER_ROLE = "WINNER"

    //Функция сохраняет данные для локальной игры в SharedPreferences
    fun saveGameData(gameData: GameInfo) {
        val gson = Gson()
        val json = gson.toJson(gameData)
        preferences.edit {
            putString(GAME_DATA, json)
            apply()
        }
    }
}
