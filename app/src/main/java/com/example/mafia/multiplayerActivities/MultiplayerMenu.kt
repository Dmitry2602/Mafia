package com.example.mafia.multiplayerActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityMultiplayerMenuBinding

class MultiplayerMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onClickButtonCreateGameRoom(view: View){
        val intent = Intent(this, CreateGameRoom::class.java)
        startActivity(intent)
    }

    fun onClickButtonConnectGame(view: View){
        val intent = Intent(this, ConnectGameRoom::class.java)
        startActivity(intent)
    }
}