package com.example.mafia.multi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityGameMakerBinding

class GameMaker : AppCompatActivity() {
    private lateinit var binding: ActivityGameMakerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMakerBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
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