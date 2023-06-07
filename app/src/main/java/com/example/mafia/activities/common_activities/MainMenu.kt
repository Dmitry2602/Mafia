package com.example.mafia.activities.common_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.GameCycle
import com.example.mafia.Preferences
import com.example.mafia.activities.multiplayer_activities.MultiplayerMenu
import com.example.mafia.databinding.ActivityMainMenuBinding
import com.example.mafia.dialogs.RegistrationDialogFragment

lateinit var preferences: SharedPreferences

class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        setContentView(binding.root)

        if (!preferences.contains(Preferences.USERNAME_TAG)) {
            val registrationDialog = RegistrationDialogFragment()
            registrationDialog.isCancelable = false
            val dialogManager = supportFragmentManager
            registrationDialog.show(dialogManager, Preferences.DIALOG_TAG_REGISTER)
        }
    }

    fun onClickButtonLocalGame(view: View){
        val intent = Intent(this, GameCycle::class.java)
        startActivity(intent)
    }
    fun onClickButtonMultiplayerGame(view: View){
        val intent = Intent(this, MultiplayerMenu::class.java)
        startActivity(intent)
    }
    fun onClickButtonRules(view: View){
        val intent = Intent(this, GameHowToPlay::class.java)
        startActivity(intent)
    }
    fun onClickButtonSettings(view: View){
        val intent = Intent(this, GameSettings::class.java)
        startActivity(intent)
    }
}
