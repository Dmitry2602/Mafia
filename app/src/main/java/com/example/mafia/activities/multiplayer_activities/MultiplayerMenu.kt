package com.example.mafia.activities.multiplayer_activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityMultiplayerMenuBinding
import com.example.mafia.dialogs.JoinDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MultiplayerMenu : AppCompatActivity(), JoinDialogFragment.JoinDialogListener {
    lateinit var receivedCode: String
    private lateinit var binding: ActivityMultiplayerMenuBinding
    private lateinit var roomId: String
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getStringExtra("roomCode") ?: ""
        databaseReference = FirebaseDatabase.getInstance().reference

        // Проверяем, является ли текущий пользователь уже участником комнаты
        checkIfAlreadyJoined()
    }

    private fun checkIfAlreadyJoined() {
        val nick = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
            .getString(Preferences.USERNAME_TAG, null).toString()

        databaseReference.child("rooms").child("players")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val players = dataSnapshot.children
                    var alreadyJoined = false

                    for (playerSnapshot in players) {
                        val playerNickname = playerSnapshot.getValue(String::class.java)
                        if (playerNickname == nick) {
                            alreadyJoined = true
                            break
                        }
                    }

                    if (alreadyJoined) {
                        // Пользователь уже присоединен к комнате
                        // Выполните необходимые действия, например, отобразите экран комнаты
                    } else {
                        // Пользователь еще не присоединился к комнате
                        // Выполните необходимые действия, например, отобразите диалоговое окно для ввода кода комнаты
                        //showJoinDialog()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибки, если такая имеется
                }
            })
    }

    private fun showJoinDialog() {
        val joinDialog = JoinDialogFragment()
        joinDialog.show(supportFragmentManager, "JoinDialog")
    }

    override fun sendCode(code: String) {
        // Пользователь ввел код комнаты в диалоговом окне
        // Выполните действия для присоединения к комнате с указанным кодом
        val nick = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
            .getString(Preferences.USERNAME_TAG, null).toString()

        val playerData: HashMap<String, Any> = HashMap()
        playerData["nickname"] = nick

        databaseReference.child("rooms").child("players")
            .push().setValue(playerData) { databaseError, _ ->
                if (databaseError == null) {
                    // Присоединение к комнате прошло успешно
                    // Выполните необходимые действия, например, отобразите экран комнаты
                } else {
                    // Произошла ошибка при присоединении к комнате
                    // Обработайте ошибку соединения с базой данных
                }
            }
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

    /*override fun sendCode(code: String) {
        receivedCode = code
    }*/
}