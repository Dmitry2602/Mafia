package com.example.mafia.activities.local_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.mafia.Preferences
import com.example.mafia.R
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityGameCycleDistributionRolesBinding
import com.example.mafia.databinding.ActivityGameCycleWaitingBinding
import com.google.gson.Gson

class DistributionRoles : AppCompatActivity() {
    private lateinit var bindingWaiting: ActivityGameCycleWaitingBinding
    private lateinit var bindingDistributionRoles: ActivityGameCycleDistributionRolesBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var gameInfo: GameInfo
    private lateinit var currentPlayer: PlayerParcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWaiting = ActivityGameCycleWaitingBinding.inflate(layoutInflater)
        bindingDistributionRoles = ActivityGameCycleDistributionRolesBinding.inflate(layoutInflater)
        setContentView(bindingWaiting.root)

        preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        var json = preferences.getString(Preferences.GAME_DATA, null)
        gameInfo = gson.fromJson(json, GameInfo::class.java)
        currentPlayer = intent.getParcelableExtra(Preferences.PLAYER_DATA)!!

        bindingDistributionRoles.buttonTimer.isClickable = true
        bindingDistributionRoles.buttonTimer.setOnClickListener {
            if (gameInfo.gamePhase == GameInfo.GamePhases.Night.toString())
                return@setOnClickListener
            if (currentPlayer.id == gameInfo.players.first().id) {
                gameInfo.turn = 1
                gameInfo.gamePhase = GameInfo.GamePhases.Night.toString()

                Preferences.saveGameData(gameInfo)

                gameInfo.players.forEach { player ->
                    json = preferences.getString(Preferences.GAME_DATA, null)
                    gameInfo = gson.fromJson(json, GameInfo::class.java)
                    intent = Intent(applicationContext, DayNight::class.java)
                    intent.putExtra(Preferences.PLAYER_DATA, player)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
                }
            }
            finish()
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
         }

        bindingWaiting.textViewPlayersTurn.text = getString(R.string.textViewTurn, currentPlayer.username)
        bindingWaiting.textViewGamePhase.text = getString(R.string.textViewRoleReveal)
        bindingWaiting.buttonStartTurn.setOnClickListener {
            setContentView(bindingDistributionRoles.root)

            when (currentPlayer.role) {
                Player.Role.Mafia.toString() -> {
                    bindingDistributionRoles.imageButtonRole.setImageResource(R.drawable.role_mafia)
                    bindingDistributionRoles.textViewRole.setText(R.string.textViewYouAreMafia)
                }
                Player.Role.Civilian.toString() -> {
                    bindingDistributionRoles.imageButtonRole.setImageResource(R.drawable.role_citizens)
                    bindingDistributionRoles.textViewRole.setText(R.string.textViewYouAreCivilian)
                }
            }
            object : CountDownTimer(10_000, 1_000) {
                override fun onTick(millisUntilFinished: Long) {
                    bindingDistributionRoles.buttonTimer.text = (millisUntilFinished / 1000).toString()
                }

                override fun onFinish() {
                    bindingDistributionRoles.buttonTimer.callOnClick()
                    finish()
                    overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
                }
            }.start()
        }
    }
}