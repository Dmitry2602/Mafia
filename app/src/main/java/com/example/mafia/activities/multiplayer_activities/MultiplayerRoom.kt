package com.example.mafia.activities.multiplayer_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mafia.databinding.ActivityMultiplayerRoomBinding
import kotlin.random.Random

class MultiplayerRoom : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerRoomBinding.inflate(layoutInflater)

        val charPool : List<Char> = ('A'..'Z') + ('0'..'9')
        val code = (1..4)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
        binding.textView.text = code

        setContentView(binding.root)


    }
}