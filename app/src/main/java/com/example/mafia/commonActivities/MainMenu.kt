package com.example.mafia.commonActivities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.mafia.DistributionRoles
import com.example.mafia.R
import com.example.mafia.multiplayerActivities.MultiplayerMenu
import com.example.mafia.databinding.ActivityMainMenuBinding
import com.example.mafia.databinding.DialogRegistrationBinding
import java.lang.IllegalStateException

lateinit var preferences: SharedPreferences

class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("TABLE", Context.MODE_PRIVATE)

        //if (!preferences.contains("USERNAME")) {
        preferences.edit().clear().apply()
            val registrationDialog = RegistrationDialog()
            registrationDialog.isCancelable = false
            val dialogManager = supportFragmentManager
            registrationDialog.show(dialogManager, "Registration")
        //}
        Log.d("pref", preferences.getString("USERNAME", "empty") ?: "empty2")
    }

    fun onClickButtonLocalGame(view: View){
        val intent = Intent(this, DistributionRoles::class.java)
        binding.buttonLocalGame.text = preferences.getString("USERNAME", "empty")
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

//Вот тут вот происходит нахрюк
class RegistrationDialog : DialogFragment() {
    private lateinit var dialogBinding: DialogRegistrationBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.RegistrationDialog)
            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.dialog_registration, null))
                .setPositiveButton(R.string.toRegister) { _, _ ->
                    dialogBinding = DialogRegistrationBinding.inflate(layoutInflater)
                    val username = dialogBinding.username.text.toString()
//Если ты введёшь в диалоговое окно текст, тыкнешь кнопку и потом посмотришь лог по тегу "pref",
//то тебе выведет +, хотя должно быть +[ник]. То есть строка хранит нихуя.
//Попробуй по приколу в переменную username заносить, допустим, dialogBinding.username.hint
//или в лэйауте добавить текст по дефолту в поле username.text. Всё, пидорас, выведет.
                    Log.d("pref", "+$username")
                    val editor = preferences.edit()
                    editor.putString("USERNAME", username)
                    editor.apply()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
