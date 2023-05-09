package com.example.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityGameRulesBinding
import com.example.mafia.databinding.ActivityMainBinding

class gameRules : AppCompatActivity() {
    lateinit var binding: ActivityGameRulesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameRulesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    fun onClickCloseRules(view: View) = finish()
}