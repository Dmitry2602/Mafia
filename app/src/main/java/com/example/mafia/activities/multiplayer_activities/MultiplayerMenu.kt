package com.example.mafia.activities.multiplayer_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityMultiplayerMenuBinding
import com.example.mafia.dialogs.JoinDialogFragment

class MultiplayerMenu : AppCompatActivity(), JoinDialogFragment.JoinDialogListener {
    lateinit var receivedCode: String
    private lateinit var binding: ActivityMultiplayerMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickButtonCreateGame(view: View){
        val intent = Intent(this, MultiplayerRoom::class.java)
        startActivity(intent)
    }
    fun onClickButtonJoinGame(view: View){
        val joinDialog = JoinDialogFragment()
        joinDialog.isCancelable = false
        val dialogManager = supportFragmentManager
        joinDialog.show(dialogManager, "JOIN")
    }

    override fun sendCode(code: String) {
        receivedCode = code
    }

    fun onClickCloseRules(view: View) {}
}