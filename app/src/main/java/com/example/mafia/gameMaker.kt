package com.example.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityGameMakerBinding

class gameMaker : AppCompatActivity() {
    lateinit var binding: ActivityGameMakerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    fun onClickButtonStarGame(view: View){
        val intent = Intent(this, distributionRoles::class.java)
        startActivity(intent)
    }
}