package com.example.mafia.activities.local_activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.Preferences
import com.example.mafia.Preferences.GAME_DATA
import com.example.mafia.Preferences.TABLE_NAME
import com.example.mafia.Preferences.saveGameData
import com.example.mafia.R
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityGameCycleVictoryScreenBinding
import com.google.gson.Gson

class GameOver : AppCompatActivity() {
    private lateinit var binding: ActivityGameCycleVictoryScreenBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var gameInfo: GameInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameCycleVictoryScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = preferences.getString(GAME_DATA, null)
        gameInfo = gson.fromJson(json, GameInfo::class.java)

        binding.buttonReturnToRoom.visibility = View.GONE
        binding.buttonReturnToMenu.setOnClickListener {
            saveGameData(
                GameInfo(
                    arrayListOf(),
                    0,
                    gamePhase = GameInfo.GamePhases.Meeting.toString()
                )
            )
            finish()
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        }

        when(intent.getStringExtra(Preferences.WINNER_ROLE)) {
            Player.Role.Mafia.toString() -> {
                binding.imageButtonVictory.setImageResource(R.drawable.victory_screen_mafia)
                binding.textViewWin.setText(R.string.textViewMafiaWon)
            }
            Player.Role.Civilian.toString() -> {
                binding.imageButtonVictory.setImageResource(R.drawable.victory_screen_citizens)
                binding.textViewWin.setText(R.string.textViewCivilianWon)
            }
        }
    }
}