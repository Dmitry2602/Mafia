package com.example.mafia.multi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.mafia.R
import com.example.mafia.databinding.ActivityCreateGameRoomBinding

class CreateGameRoom : AppCompatActivity() {
    lateinit var nickname: EditText
    lateinit var playerCounter: EditText
    lateinit var gameCode: EditText
    lateinit var binding: ActivityCreateGameRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGameRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}