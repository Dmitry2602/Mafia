package com.example.mafia.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityGameRulesBinding

class GameRules : AppCompatActivity() {
    private lateinit var binding: ActivityGameRulesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameRulesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    fun onClickCloseRules(view: View) = finish()
}