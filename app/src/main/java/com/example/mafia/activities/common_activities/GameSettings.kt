package com.example.mafia.activities.common_activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityGameSettingsBinding
import com.example.mafia.dialogs.RegistrationDialogFragment
import com.google.firebase.database.DatabaseReference

class GameSettings : AppCompatActivity() {
    private lateinit var binding: ActivityGameSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameSettingsBinding.inflate(layoutInflater)

        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        binding.editTextText.setText(preferences.getString(Preferences.USERNAME_TAG, null))
        binding.editTextText.doOnTextChanged { text, _, _, _ ->
            val username = text.toString()
            val editor = com.example.mafia.activities.common_activities.preferences.edit()
            editor.putString(Preferences.USERNAME_TAG, username)
            editor.apply()
        }
        setContentView(binding.root)
    }

    fun onClickCloseSettings(view: View) = finish()
}