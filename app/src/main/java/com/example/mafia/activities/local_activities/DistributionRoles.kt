package com.example.mafia.activities.local_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.mafia.Preferences.GAME_DATA
import com.example.mafia.Preferences.TABLE_NAME
import com.example.mafia.Preferences.saveGameData
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

        preferences = getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = preferences.getString(GAME_DATA, null)
        gameInfo = gson.fromJson(json, GameInfo::class.java)
        currentPlayer = gameInfo.players.first()

        bindingDistributionRoles.buttonTimer.isClickable = true
        bindingDistributionRoles.buttonTimer.setOnClickListener {
            countDownTimer.cancel()
            if (currentPlayer == gameInfo.players.last()) {
                gameInfo.turn = 1
                gameInfo.gamePhase = GameInfo.GamePhases.Night.toString()
                saveGameData(gameInfo)

                intent = Intent(applicationContext, DayNight::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
            } else {
                currentPlayer = gameInfo.players[currentPlayer.id + 1]
                startTurn()
            }
        }
        setContentView(bindingWaiting.root)
        bindingWaiting.textViewPlayersTurn.text = getString(R.string.textViewTurn, currentPlayer.username)
        bindingWaiting.textViewGamePhase.text = getString(R.string.textViewRoleReveal)
        startTurn()
    }

    private fun startTurn() {
        if (currentPlayer != gameInfo.players.first()) {
            setContentView(bindingWaiting.root)
            bindingWaiting.textViewPlayersTurn.text = getString(R.string.textViewTurn, currentPlayer.username)
            bindingWaiting.textViewGamePhase.text = getString(R.string.textViewRoleReveal)
        }
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
            countDownTimer.start()
        }
    }

    private val countDownTimer = object : CountDownTimer(10_000, 1_000) {
        override fun onTick(millisUntilFinished: Long) {
            val timeRemaining = (millisUntilFinished / 1000).toString()
            bindingDistributionRoles.buttonTimer.text = timeRemaining
        }
        override fun onFinish() { bindingDistributionRoles.buttonTimer.callOnClick() }
    }
}