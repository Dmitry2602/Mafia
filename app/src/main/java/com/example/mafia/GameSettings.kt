package com.example.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.databinding.ActivityGameSettingsBinding

class GameSettings : AppCompatActivity() {
    private lateinit var binding: ActivityGameSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    fun onClickCloseSettings(view: View) = finish()
}