package com.example.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.mafia.databinding.ActivityGameMakerBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GameMaker : AppCompatActivity() {
    private lateinit var binding: ActivityGameMakerBinding
    private lateinit var linearLayout: LinearLayout
    private lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMakerBinding.inflate(layoutInflater)
        linearLayout = binding.linearLayout
        addButton = binding.buttonAdd

        addButton.setOnClickListener {
            addEditText()
        }
        setContentView(binding.root)
        supportActionBar?.hide()
    }
    private fun addEditText() {
        val editText = EditText(this)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayout.addView(editText)
        // Устанавливаем отступ между EditText
        val params = editText.layoutParams as LinearLayout.LayoutParams
        params.topMargin = resources.getDimensionPixelSize(R.dimen.edit_text_margin_top)
        editText.layoutParams = params
       // linearLayout.addView(editText)
    }
    fun onClickButtonStartGame(view: View){
        val intent = Intent(this, DistributionRoles::class.java)
        startActivity(intent)
    }
}