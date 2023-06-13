package com.example.mafia.activities.common_activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityGameSettingsBinding


class GameSettings : AppCompatActivity() {
    private lateinit var binding: ActivityGameSettingsBinding
    private val editor = preferences.edit()
    private val username = preferences.getString(Preferences.USERNAME_TAG, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameSettingsBinding.inflate(layoutInflater)
        binding.editTextUsername.setText(preferences.getString(Preferences.USERNAME_TAG, null))
        binding.editTextUsername.doOnTextChanged { text, _, _, _ ->
            val username = text.toString()
            editor.putString(Preferences.USERNAME_TAG, username)
            editor.apply()
        }

        setContentView(binding.root)
    }

    fun onClickCloseSettings(view: View) {
        if (binding.editTextUsername.text.isNullOrEmpty())
            binding.editTextUsername.setText(username)
        finish()
    }

    fun onClickReset(view: View) {
        editor.clear()
        editor.apply()
        finishAffinity()
    }
}