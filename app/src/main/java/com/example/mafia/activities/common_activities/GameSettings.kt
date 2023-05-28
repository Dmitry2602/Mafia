package com.example.mafia.activities.common_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.mafia.databinding.ActivityGameSettingsBinding
import com.google.firebase.database.DatabaseReference

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