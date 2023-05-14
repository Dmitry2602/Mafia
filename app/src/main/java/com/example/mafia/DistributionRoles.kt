package com.example.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mafia.databinding.ActivityDistributionRolesBinding

class DistributionRoles : AppCompatActivity() {
    private lateinit var binding: ActivityDistributionRolesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistributionRolesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}