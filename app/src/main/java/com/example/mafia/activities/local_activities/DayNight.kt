package com.example.mafia.activities.local_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mafia.Preferences
import com.example.mafia.Preferences.GAME_DATA
import com.example.mafia.Preferences.PLAYER_DATA
import com.example.mafia.Preferences.TABLE_NAME
import com.example.mafia.Preferences.WINNER_ROLE
import com.example.mafia.Preferences.saveGameData
import com.example.mafia.R
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityGameCycleDayResultBinding
import com.example.mafia.databinding.ActivityGameCycleNightBinding
import com.example.mafia.databinding.ActivityGameCycleWaitingBinding
import com.google.gson.Gson

class DayNight : AppCompatActivity(), VotingAdapterLocal.RecyclerViewRevotedListener {
    private lateinit var bindingWaiting: ActivityGameCycleWaitingBinding
    private lateinit var bindingVote: ActivityGameCycleNightBinding
    private lateinit var bindingResult: ActivityGameCycleDayResultBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var gameInfo: GameInfo
    private lateinit var currentPlayer: PlayerParcelable

    //Функция возвращает в прежнее положение (отжимает) прошлый нажатый элемент в RecyclerView
    override fun restoreRecyclerView() {
        val viewHolder = bindingVote.recyclerViewVoting.children.find { !it.findViewById<Button>(R.id.buttonVote).isEnabled } ?: return
        viewHolder.findViewById<Button>(R.id.buttonVote).isEnabled = true
        val counter = viewHolder.findViewById<TextView>(R.id.imageViewVotes)
        counter.text = (counter.text.toString().toIntOrNull() !!- 1).toString()
        if (counter.text == "0")
            counter.visibility = View.INVISIBLE
    }

    private fun calculateVotes() {
        val voted = bindingVote.recyclerViewVoting.children.find { !it.findViewById<TextView>(R.id.buttonVote).isEnabled }
        if (voted != null) {
            val player = gameInfo.players.find { it.username == voted.findViewById<Button>(R.id.buttonVote).text.toString() }
            gameInfo.players.asReversed()[player!!.id].gotVotes += 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWaiting = ActivityGameCycleWaitingBinding.inflate(layoutInflater)
        bindingVote = ActivityGameCycleNightBinding.inflate(layoutInflater)

        preferences = getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = preferences.getString(GAME_DATA, null)
        gameInfo = gson.fromJson(json, GameInfo::class.java)
        currentPlayer = intent.getParcelableExtra(PLAYER_DATA)!!

        setContentView(bindingWaiting.root)

        bindingWaiting.textViewPlayersTurn.text = getString(R.string.textViewTurn, currentPlayer.username)
        when (gameInfo.gamePhase) {
            GameInfo.GamePhases.Night.toString() ->
                bindingWaiting.textViewGamePhase.text = getString(R.string.textViewNight, gameInfo.turn)
            GameInfo.GamePhases.Day.toString() ->
                bindingWaiting.textViewGamePhase.text = getString(R.string.textViewDay, gameInfo.turn)
        }
        bindingWaiting.buttonStartTurn.setOnClickListener { startTurn() }

        bindingVote.buttonTimer.isClickable = true
        bindingVote.buttonTimer.setOnClickListener {
            when (gameInfo.gamePhase) {
                GameInfo.GamePhases.Night.toString() -> {
                    if (currentPlayer.role == Player.Role.Mafia.toString())
                        calculateVotes()
                    saveGameData(gameInfo)

                    if (currentPlayer.id == gameInfo.players.first().id) {
                        gameInfo.gamePhase = GameInfo.GamePhases.Day.toString()
                        val max = gameInfo.players.maxBy { it.gotVotes }.gotVotes
                        var victimCounter = 0
                        gameInfo.players.forEach { player ->
                            if (player.gotVotes == max) {
                                player.wasKilled = true
                                victimCounter += 1
                            }
                            player.gotVotes = 0
                        }
                        if (victimCounter > 1) {
                            gameInfo.players.forEach { player ->
                                player.wasKilled = false
                            }
                        }
                    }
                }

                GameInfo.GamePhases.Day.toString() -> {
                    calculateVotes()
                    saveGameData(gameInfo)

                    if (currentPlayer.id == gameInfo.players.first().id) {
                        val max = gameInfo.players.maxBy { it.gotVotes }.gotVotes
                        var victimCounter = 0
                        gameInfo.players.forEach { player ->
                            if (player.gotVotes == max) {
                                player.wasKicked = true
                                victimCounter += 1
                            }
                            player.gotVotes = 0
                        }
                        if (victimCounter > 1) {
                            gameInfo.players.forEach { player ->
                                player.wasKicked = false
                            }
                        }

                        intent = Intent(applicationContext, DayResults::class.java)
                        if (gameInfo.players.find { it.wasKicked } == null) {
                            if (gameInfo.players.find { it.wasKilled } == null) {
                                gameInfo.gamePhase = GameInfo.GamePhases.Night.toString()
                                gameInfo.turn += 1
                            }
                            else {
                                gameInfo.gamePhase = GameInfo.GamePhases.MafiaResult.toString()
                                saveGameData(gameInfo)
                                startActivity(intent)
                            }
                        } else {
                            gameInfo.gamePhase = GameInfo.GamePhases.GeneralResult.toString()
                            saveGameData(gameInfo)
                            startActivity(intent)
                        }
                    }
                }
            }
            saveGameData(gameInfo)
            if (currentPlayer.id == gameInfo.players.first().id) {
                gameInfo.players.forEach { player ->
                    intent = Intent(applicationContext, DayNight::class.java)
                    intent.putExtra(PLAYER_DATA, player)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
                }
            }
            finish()
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        }
    }
    private fun startTurn() {
        when (gameInfo.gamePhase) {
            GameInfo.GamePhases.Night.toString() -> {
                setContentView(bindingVote.root)
                bindingVote.textViewNight.text = getString(R.string.textViewNight, gameInfo.turn)
                when (currentPlayer.role) {
                    Player.Role.Mafia.toString() -> {
                        val manager = LinearLayoutManager(applicationContext)
                        //Класс VotingAdapter отвечает за обработку и отображение Item в RecyclerView
                        val adapter = VotingAdapterLocal()
                        //В варианты для голосования у мафии добавляются все мирные живые жители
                        adapter.playersList.addAll(
                            buildList {
                                gameInfo.players.forEach { player ->
                                    if (player.role == Player.Role.Civilian.toString()
                                        && !player.wasKilled
                                        && !player.wasKicked) add(player)
                                }
                            }.reversed()
                        )

                        bindingVote.recyclerViewVoting.layoutManager = manager
                        bindingVote.recyclerViewVoting.adapter = adapter
                    }

                    Player.Role.Civilian.toString() -> {
                        //RecyclerView отсутствует у мирных жителей
                        bindingVote.recyclerViewVoting.visibility = View.GONE
                    }

                }
                object : CountDownTimer(10_000, 1_000) {
                    override fun onTick(millisUntilFinished: Long) {
                        bindingVote.buttonTimer.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        bindingVote.buttonTimer.callOnClick()
                        finish()
                        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
                    }
                }.start()
            }
            GameInfo.GamePhases.Day.toString() -> {
                setContentView(bindingVote.root)
                bindingVote.textViewNight.text = getString(R.string.textViewDay, gameInfo.turn)
                val manager = LinearLayoutManager(applicationContext)
                val adapter = VotingAdapterLocal()
                //В вариантах для голосования все игроки
                adapter.playersList.addAll(gameInfo.players.minus(gameInfo.players.find { it.id == currentPlayer.id }!!).reversed())

                bindingVote.recyclerViewVoting.layoutManager = manager
                bindingVote.recyclerViewVoting.adapter = adapter

                object : CountDownTimer(10_000, 1_000) {
                    override fun onTick(millisUntilFinished: Long) {
                        bindingVote.buttonTimer.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        bindingVote.buttonTimer.callOnClick()
                    }
                }.start()
            }
        }
    }
}