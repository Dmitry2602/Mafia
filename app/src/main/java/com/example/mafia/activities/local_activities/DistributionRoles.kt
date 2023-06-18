package com.example.mafia.activities.local_activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.mafia.Preferences
import com.example.mafia.R
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityGameCycleDistributionRolesBinding
import com.example.mafia.databinding.ActivityGameCycleWaitingBinding

class DistributionRoles : AppCompatActivity(), DayNight.ActivityFinishedListener {
    private lateinit var bindingWaiting: ActivityGameCycleWaitingBinding
    private lateinit var bindingDistributionRoles: ActivityGameCycleDistributionRolesBinding

    private lateinit var gameInfo: GameInfoParcelable
    private var id: Int? = null

    //Заменяет registerForActivityResult, т.к. она поломана циклом
    override fun sendGameInfo(sentGameInfo: GameInfoParcelable) {
        gameInfo = sentGameInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWaiting = ActivityGameCycleWaitingBinding.inflate(layoutInflater)
        bindingDistributionRoles = ActivityGameCycleDistributionRolesBinding.inflate(layoutInflater)
        setContentView(bindingWaiting.root)

        gameInfo = intent.getParcelableExtra(Preferences.GAME_DATA)!!
        id = intent.getIntExtra(Preferences.PLAYER_ID, -1)

        bindingDistributionRoles.buttonTimer.isClickable = true
        bindingDistributionRoles.buttonTimer.setOnClickListener {
            if (gameInfo.gamePhase == Preferences.GamePhases.Night.toString())
                return@setOnClickListener
            if (gameInfo.players!![id!!] == gameInfo.players!!.last()) {
                gameInfo.turn = 1
                gameInfo.gamePhase = Preferences.GamePhases.Night.toString()

                gameInfo.players!!.forEach { player ->
                    intent = Intent(applicationContext, DayNight::class.java)
                    intent.putExtra(Preferences.GAME_DATA, gameInfo)
                    intent.putExtra(Preferences.PLAYER_ID, player.playerId)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            }
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
         }

        bindingWaiting.textViewPlayersTurn.text = getString(R.string.textViewTurn, gameInfo.players!![id!!].username)
        bindingWaiting.textViewGamePhase.text = getString(R.string.textViewRoleReveal)
        bindingWaiting.buttonStartTurn.setOnClickListener {
            setContentView(bindingDistributionRoles.root)

            when (gameInfo.players!![id!!].role) {
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
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            }.start()
        }
    }
}