package com.example.mafia.activities.multiplayer_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mafia.databinding.ActivityMultiplayerMenuBinding

class MultiplayerMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}