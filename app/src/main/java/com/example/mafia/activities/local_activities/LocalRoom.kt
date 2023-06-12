package com.example.mafia.activities.local_activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.PlayerAdapter
import com.example.mafia.Preferences
import com.example.mafia.activities.common_activities.preferences
import com.example.mafia.databinding.ActivityLocalRoomBinding
import com.example.mafia.databinding.RecyclerViewPlayerBinding


class LocalRoom : AppCompatActivity() {
    private lateinit var binding: ActivityLocalRoomBinding
    private lateinit var adapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    fun onClickButtonStartGame(view: View){
        val intent = Intent(this, GameCycleLocal::class.java)
        startActivity(intent)
    }
}
