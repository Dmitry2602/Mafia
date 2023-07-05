package com.example.mafia.activities.local_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.mafia.Preferences
import com.example.mafia.Preferences.saveGameData
import com.example.mafia.R
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityGameCycleDayResultBinding
import com.example.mafia.databinding.ActivityGameCycleNightBinding
import com.google.gson.Gson

class DayResults : AppCompatActivity() {
    private lateinit var binding: ActivityGameCycleDayResultBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var gameInfo: GameInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameCycleDayResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = preferences.getString(Preferences.GAME_DATA, null)
        gameInfo = gson.fromJson(json, GameInfo::class.java)

        val kickedPlayer: PlayerParcelable? = gameInfo.players.find { it.wasKicked }
        val killedPlayer: PlayerParcelable? = gameInfo.players.find { it.wasKilled }

        binding.buttonTimer.isClickable = true
        binding.buttonTimer.setOnClickListener {
            if (killedPlayer == null) {
                gameInfo.gamePhase = GameInfo.GamePhases.Night.toString()
                gameInfo.turn += 1
            } else
                gameInfo.gamePhase = GameInfo.GamePhases.MafiaResult.toString()

            if (kickedPlayer != null)
                gameInfo.players.remove(kickedPlayer)
            else if (killedPlayer != null) {
                gameInfo.gamePhase = GameInfo.GamePhases.Night.toString()
                gameInfo.turn += 1
                gameInfo.players.remove(killedPlayer)
            }

            saveGameData(gameInfo)
            intent = Intent(applicationContext, DayNight::class.java)

            when (gameInfo.players.count { it.role == Player.Role.Mafia.toString() }) {
                0 -> {
                    intent = Intent(applicationContext, GameOver::class.java)
                    intent.putExtra(Preferences.WINNER_ROLE, Player.Role.Civilian.toString())
                }

                1 -> {
                    if (gameInfo.players.size == 2) {
                        intent = Intent(applicationContext, GameOver::class.java)
                        intent.putExtra(Preferences.WINNER_ROLE, Player.Role.Mafia.toString())
                    }
                }

                in gameInfo.players.size / 2 + 1..gameInfo.players.size -> {
                    intent = Intent(applicationContext, GameOver::class.java)
                    intent.putExtra(Preferences.WINNER_ROLE, Player.Role.Mafia.toString())
                }
            }
            intent.putExtra(Preferences.PLAYER_DATA, gameInfo.players.last())
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        }

        when (gameInfo.gamePhase) {
            GameInfo.GamePhases.GeneralResult.toString() -> {
                binding.imageButtonVoteResult.setImageResource(R.drawable.kicked_player)
                binding.textViewVoteResult.text =
                    getString(R.string.textViewPlayerWasKicked, kickedPlayer!!.username)
                object : CountDownTimer(60_000, 1_000) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.buttonTimer.text =
                            (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        binding.buttonTimer.callOnClick()
                    }
                }.start()
            }

            GameInfo.GamePhases.MafiaResult.toString() -> {
                binding.imageButtonVoteResult.setImageResource(R.drawable.dead_player)
                binding.textViewVoteResult.text =
                    getString(R.string.textViewPlayerWasKilled, killedPlayer!!.username)
                object : CountDownTimer(60_000, 1_000) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.buttonTimer.text =
                            (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        binding.buttonTimer.callOnClick()
                    }
                }.start()
            }
        }
    }
}