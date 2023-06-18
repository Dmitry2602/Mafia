package com.example.mafia.activities.local_activities

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
import com.example.mafia.R
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityGameCycleDayResultBinding
import com.example.mafia.databinding.ActivityGameCycleNightBinding
import com.example.mafia.databinding.ActivityGameCycleWaitingBinding

class DayNight : AppCompatActivity(), VotingAdapterLocal.RecyclerViewRevotedListener {
    private lateinit var bindingWaiting: ActivityGameCycleWaitingBinding
    private lateinit var bindingVote: ActivityGameCycleNightBinding
    private lateinit var bindingResult: ActivityGameCycleDayResultBinding

    private lateinit var listener: ActivityFinishedListener

    interface ActivityFinishedListener {
        fun sendGameInfo(sentGameInfo: GameInfoParcelable)
    }

    private lateinit var gameInfo: GameInfoParcelable
    private var id: Int? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        listener = bindingVote.recyclerViewVoting.context as ActivityFinishedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWaiting = ActivityGameCycleWaitingBinding.inflate(layoutInflater)
        bindingVote = ActivityGameCycleNightBinding.inflate(layoutInflater)
        bindingResult = ActivityGameCycleDayResultBinding.inflate(layoutInflater)
        setContentView(bindingWaiting.root)

        @Suppress("DEPRECATION")
        gameInfo = intent.getParcelableExtra(Preferences.GAME_DATA)!!
        id = intent.getIntExtra(Preferences.PLAYER_ID, -1)

        bindingWaiting.textViewPlayersTurn.text = getString(R.string.textViewTurn, gameInfo.players!![id!!].username)
        bindingWaiting.textViewGamePhase.text = getString(R.string.textViewNight, gameInfo.turn)
        bindingWaiting.buttonStartTurn.setOnClickListener { startTurn() }

        bindingVote.buttonTimer.isClickable = true
        bindingVote.buttonTimer.setOnClickListener {
            if (gameInfo.players!![id!!].role == Player.Role.Mafia.toString()) {
                val voted = bindingVote.recyclerViewVoting.children.find { !it.findViewById<TextView>(R.id.buttonVote).isEnabled }
                val player = gameInfo.players!!.find { it.username == voted!!.findViewById<Button>(R.id.buttonVote).text.toString() }
                gameInfo.players!![player!!.playerId] = player
            }
            listener.sendGameInfo(gameInfo)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun startTurn() {
        when (gameInfo.gamePhase) {
            Preferences.GamePhases.Night.toString() -> {
                setContentView(bindingVote.root)
                bindingVote.textViewNight.text = getString(R.string.textViewNight, gameInfo.turn)
                when (gameInfo.players!![id!!].role) {
                    Player.Role.Mafia.toString() -> {
                        val manager = LinearLayoutManager(applicationContext)
                        //Класс VotingAdapter отвечает за обработку и отображение Item в RecyclerView
                        val adapter = VotingAdapterLocal()
                        //В варианты для голосования у мафии добавляются все мирные живые жители
                        adapter.playersList.addAll(
                            buildList {
                                gameInfo.players!!.forEach {player ->
                                    if (player.role == Player.Role.Civilian.toString() && player.isAlive) add(player)
                                }
                            }
                        )

                        bindingVote.recyclerViewVoting.layoutManager = manager
                        bindingVote.recyclerViewVoting.adapter = adapter
                    }

                    Player.Role.Civilian.toString() -> {
                        //RecyclerView отсутствует у мирных жителей
                        bindingVote.recyclerViewVoting.visibility = View.GONE
                    }

                }
                object : CountDownTimer(60_000, 1_000) {
                    override fun onTick(millisUntilFinished: Long) {
                        bindingVote.buttonTimer.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        bindingVote.buttonTimer.callOnClick()
                        finish()
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                }.start()
            }
            Preferences.GamePhases.Day.toString() -> { TODO("Сделать утреннюю часть игры") }
        }
    }
    //Функция возвращает в прежнее положение (отжимает) прошлый нажатый элемент в RecyclerView
    override fun restoreRecyclerView() {
        val viewHolder = bindingVote.recyclerViewVoting.children.find { !it.findViewById<Button>(R.id.buttonVote).isEnabled } ?: return
        viewHolder.findViewById<Button>(R.id.buttonVote).isEnabled = true
        val counter = viewHolder.findViewById<TextView>(R.id.imageViewVotes)
        counter.text = (counter.text.toString().toIntOrNull() !!- 1).toString()
        if (counter.text == "0")
            counter.visibility = View.INVISIBLE
    }
}