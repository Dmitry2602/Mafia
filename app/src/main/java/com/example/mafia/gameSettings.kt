package com.example.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityGameRulesBinding
import com.example.mafia.databinding.ActivityGameSettingsBinding
import com.example.mafia.databinding.ActivityMainBinding

class gameSettings : AppCompatActivity() {
    lateinit var binding: ActivityGameSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    fun onClickCloseSettings(view: View) = finish()
}