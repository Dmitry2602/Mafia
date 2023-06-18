package com.example.mafia.activities.local_activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mafia.PlayerAdapter
import com.example.mafia.Preferences
import com.example.mafia.R
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityLocalRoomBinding


class LocalRoom : AppCompatActivity(), PlayerAdapter.RecyclerViewChangedListener {
    private lateinit var binding: ActivityLocalRoomBinding
    private lateinit var adapter: PlayerAdapter

    override fun sendItemCount(itemCount: Int) {
        binding.buttonStartGame.isEnabled = itemCount >= 4
    }

    private val gameInfo = GameInfoParcelable(
        players = arrayListOf(),
        turn = 0,
        gamePhase = Preferences.GamePhases.Meeting.toString()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = LinearLayoutManager(this)
        adapter = PlayerAdapter()
        adapter.playersList = //MutableList(4) {null}
            MutableList(6) { "Player ${it + 1}" }

        binding.recyclerViewPlayers.layoutManager = manager
        binding.recyclerViewPlayers.adapter = adapter
    }

    fun onClickButtonAddPlayer(view: View) {
        adapter.playersList.add(null)
        binding.recyclerViewPlayers.adapter = adapter
    }

    fun onClickButtonStartGame(view: View){
        val usernames = mutableListOf<String>()
        binding.recyclerViewPlayers.children.forEach { viewHolder ->
            usernames.add(viewHolder.findViewById<EditText>(R.id.editTextPlayer).text.toString())
        }

        val rolesPool = mutableListOf<String>()
        repeat(usernames.size / 3) {
            rolesPool.add(Player.Role.Mafia.toString())
        }
        repeat(usernames.size - rolesPool.size) {
            rolesPool.add(Player.Role.Civilian.toString())
        }
        rolesPool.shuffle()
        usernames.forEachIndexed { index, player ->
            gameInfo.players!!.add(
                PlayerParcelable(
                    playerId = index,
                    username = player,
                    role = rolesPool[index],
                    isAlive = true,
                    gotVotes = 0
                )
            )
        }
        gameInfo.players!!.reverse()
        gameInfo.players!!.forEach { player ->
            val intent = Intent(this, DistributionRoles::class.java)
            intent.putExtra(Preferences.GAME_DATA, gameInfo)
            intent.putExtra(Preferences.PLAYER_ID, player.playerId)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}
