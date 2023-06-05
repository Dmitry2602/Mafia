package com.example.mafia.activities.common_activities

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

        binding.buttonCloseSettings.setOnClickListener { finish() }
    }

    fun onClickCloseRules(view: View) = finish()
}