package com.example.mafia.activities.local_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.mafia.Preferences
import com.example.mafia.Preferences.GAME_DATA
import com.example.mafia.Preferences.TABLE_NAME
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

        preferences = getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = preferences.getString(GAME_DATA, null)
        gameInfo = gson.fromJson(json, GameInfo::class.java)

        val kickedPlayer: PlayerParcelable? = gameInfo.players.find { it.wasKicked }
        val killedPlayer: PlayerParcelable? = gameInfo.players.find { it.wasKilled }


        when (gameInfo.gamePhase) {
            GameInfo.GamePhases.MafiaResult.toString() -> {
                if (killedPlayer != null) {
                    binding.imageButtonVoteResult.setImageResource(R.drawable.dead_player)
                    binding.textViewVoteResult.text = getString(R.string.textViewPlayerWasKilled, killedPlayer.username)
                    gameInfo.players.remove(killedPlayer)
                    gameInfo.players.forEachIndexed { index, player ->
                        player.id = index
                    }
                } else {
                    binding.imageButtonVoteResult.visibility = View.GONE
                    binding.textViewVoteResult.text = getString(R.string.textNoOneWasKilled)
                }

                binding.buttonContinue.setOnClickListener {
                    gameInfo.gamePhase = GameInfo.GamePhases.Day.toString()
                    saveGameData(gameInfo)

                    intent = returnIntent()
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
                }
            }
            GameInfo.GamePhases.GeneralResult.toString() -> {
                if (kickedPlayer != null) {
                    binding.imageButtonVoteResult.setImageResource(R.drawable.kicked_player)
                    binding.textViewVoteResult.text = getString(R.string.textViewPlayerWasKicked, kickedPlayer.username)
                    gameInfo.players.remove(kickedPlayer)
                    gameInfo.players.forEachIndexed { index, player ->
                        player.id = index
                    }
                } else {
                    binding.imageButtonVoteResult.visibility = View.GONE
                    binding.textViewVoteResult.text = getString(R.string.textNoOneWasKicked)
                }

                binding.buttonContinue.setOnClickListener {
                    gameInfo.gamePhase = GameInfo.GamePhases.Night.toString()
                    gameInfo.turn += 1
                    saveGameData(gameInfo)

                    intent = returnIntent()
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
                }
            }
        }
    }

    private fun returnIntent(): Intent {
        when (gameInfo.players.count { it.role == Player.Role.Mafia.toString() }) {
            0 -> {
                intent = Intent(applicationContext, GameOver::class.java)
                intent.putExtra(Preferences.WINNER_ROLE, Player.Role.Civilian.toString())
            }
            1 -> {
                if (gameInfo.players.size == 2) {
                    intent = Intent(applicationContext, GameOver::class.java)
                    intent.putExtra(Preferences.WINNER_ROLE, Player.Role.Mafia.toString())
                } else {
                    intent = Intent(applicationContext, DayNight::class.java)
                }
            }
            in gameInfo.players.size / 2 + 1..gameInfo.players.size -> {
                intent = Intent(applicationContext, GameOver::class.java)
                intent.putExtra(Preferences.WINNER_ROLE, Player.Role.Mafia.toString())
            }
            else -> intent = Intent(applicationContext, DayNight::class.java)
        }
        return intent
    }
}