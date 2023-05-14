package com.example.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityMainBinding

class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

    }
    fun onClickButtonMakeGame(view: View){
        val intent = Intent(this, GameMaker::class.java)
        startActivity(intent)
    }
    fun onClickButtonRules(view: View){
        val intent = Intent(this, GameRules::class.java)
        startActivity(intent)
    }
    fun onClickButtonSettings(view: View){
        val intent = Intent(this,GameSettings::class.java)
        startActivity(intent)
    }
}