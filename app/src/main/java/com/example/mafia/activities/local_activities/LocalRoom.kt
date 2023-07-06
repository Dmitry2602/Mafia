package com.example.mafia.activities.local_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mafia.PlayerAdapter
import com.example.mafia.Preferences
import com.example.mafia.Preferences.saveGameData
import com.example.mafia.R
import com.example.mafia.databinding.ActivityLocalRoomBinding

class LocalRoom : AppCompatActivity(), PlayerAdapter.RecyclerViewChangedListener {
    private lateinit var binding: ActivityLocalRoomBinding
    private lateinit var adapter: PlayerAdapter
    private lateinit var preferences: SharedPreferences

    override fun sendItemCount(itemCount: Int) {
        binding.buttonStartGame.isEnabled = itemCount >= 4
    }

    override fun sendTextChanged(text: String) {
        binding.buttonStartGame.isEnabled = text.isNotEmpty()
    }

    private val gameInfo = GameInfo(
        players = arrayListOf(),
        turn = 0,
        gamePhase = GameInfo.GamePhases.Meeting.toString()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)

        val manager = LinearLayoutManager(this)
        adapter = PlayerAdapter()
        adapter.playersList = MutableList(4) {null}

        binding.recyclerViewPlayers.layoutManager = manager
        binding.recyclerViewPlayers.adapter = adapter
    }

    fun onClickButtonAddPlayer(view: View) {
        adapter.playersList.add(null)
        binding.recyclerViewPlayers.adapter = adapter
    }

    fun onClickButtonStartGame(view: View) {
        val usernames = mutableListOf<String>()
        binding.recyclerViewPlayers.children.forEach { viewHolder ->
            usernames.add(viewHolder.findViewById<EditText>(R.id.editTextPlayer).text.toString())
        }

        val rolesPool = mutableListOf<String>()
        repeat(usernames.size / 3) {
            rolesPool.add(PlayerParcelable.Role.Mafia.toString())
        }
        repeat(usernames.size - rolesPool.size) {
            rolesPool.add(PlayerParcelable.Role.Civilian.toString())
        }
        rolesPool.shuffle()
        usernames.forEachIndexed { index, player ->
            gameInfo.players.add(
                PlayerParcelable(
                    id = index,
                    username = player,
                    role = rolesPool[index],
                    wasKilled = false,
                    wasKicked = false,
                    gotVotes = 0
                )
            )
        }

        saveGameData(gameInfo)
        val intent = Intent(this, DistributionRoles::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
    }
}
