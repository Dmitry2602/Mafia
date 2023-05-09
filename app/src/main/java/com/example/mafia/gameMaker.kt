package com.example.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mafia.databinding.ActivityGameMakerBinding

class gameMaker : AppCompatActivity() {
    lateinit var binding: ActivityGameMakerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}