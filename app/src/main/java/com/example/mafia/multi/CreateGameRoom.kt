package com.example.mafia.multi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.mafia.R
import com.example.mafia.databinding.ActivityDistributionRolesBinding

class CreateGameRoom : AppCompatActivity() {
    lateinit var nickname: EditText
    lateinit var playerCounter: EditText
    lateinit var gameCode: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_registration)
        supportActionBar?.hide()
    }
}