package com.example.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

    }
    fun onClickButtonMakeGame(view: View){
        val intent = Intent(this, gameMaker::class.java)
        startActivity(intent)
    }
    fun onClickButtonRules(view: View){
        val intent = Intent(this, gameRules::class.java)
        startActivity(intent)
    }
    fun onClickButtonSettings(view: View){
        val intent = Intent(this,gameSettings::class.java)
        startActivity(intent)
    }
}