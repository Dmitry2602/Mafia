package com.example.mafia.commonActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.DistributionRoles
import com.example.mafia.multiplayerActivities.MultiplayerMenu
import com.example.mafia.databinding.ActivityMainMenuBinding

class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onClickButtonLocalGame(view: View){
        val intent = Intent(this, DistributionRoles::class.java)
        startActivity(intent)
    }
    fun onClickButtonMultiplayerGame(view: View){
        val intent = Intent(this, MultiplayerMenu::class.java)
        startActivity(intent)
    }
    fun onClickButtonRules(view: View){
        val intent = Intent(this, GameRules::class.java)
        startActivity(intent)
    }
    fun onClickButtonSettings(view: View){
        val intent = Intent(this, GameSettings::class.java)
        startActivity(intent)
    }
}