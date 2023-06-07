package com.example.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.mafia.databinding.ActivityDistributionRolesBinding

class GameCycle : AppCompatActivity() {
    private lateinit var binding: ActivityDistributionRolesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistributionRolesBinding.inflate(layoutInflater)

        //При создании активити будет запущен таймер на 60 секунд
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.buttonTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                TODO("лэйаут сменяется на следующий & таймер начинает отчёт сначала")
            }
        }.start()

        setContentView(binding.root)
    }
}