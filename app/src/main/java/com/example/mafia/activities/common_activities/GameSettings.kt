package com.example.mafia.activities.common_activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityGameSettingsBinding
import com.google.firebase.database.DatabaseReference

class GameSettings : AppCompatActivity() {
    private lateinit var binding: ActivityGameSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        binding.textView2.text = preferences.getString(Preferences.USERNAME_TAG, "Никнейм отсутствует")
    }

    fun onClickCloseSettings(view: View) = finish()
}