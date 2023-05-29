package com.example.mafia.activities.common_activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mafia.DistributionRoles
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
        setContentView(binding.root)

        preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        preferences.getString(Preferences.USERNAME_TAG, null)
        if (!preferences.contains(Preferences.USERNAME_TAG)) {
            val registrationDialog = RegistrationDialogFragment()
            registrationDialog.isCancelable = false
            val dialogManager = supportFragmentManager
            registrationDialog.show(dialogManager, Preferences.DIALOG_TAG)
        }
    }

    fun onClickButtonLocalGame(view: View){
        val intent = Intent(this, DistributionRoles::class.java)
        startActivity(intent)
    }
    fun onClickButtonMultiplayerGame(view: View){
        val intent = Intent(this, MultiplayerMenu::class.java)
        startActivity(intent)
    }
    fun onClickButtonRules(view: View){
        val intent = Intent(this, GameRules::class.java)
        startActivity(intent)
    }
    fun onClickButtonSettings(view: View){
        val intent = Intent(this, GameSettings::class.java)
        startActivity(intent)
    }
}