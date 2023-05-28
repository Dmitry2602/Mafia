package com.example.mafia.multiplayerActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mafia.databinding.ActivityMultiplayerMenuBinding

//lateinit var preferences: SharedPreferences

class MultiplayerMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        //preferences = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
       // binding.textView2.text = preferences.getString("USERNAME", "лох")
        setContentView(binding.root)
    }

}