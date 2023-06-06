package com.example.mafia.activities.multiplayer_activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mafia.DistributionRoles
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityMultiplayerMenuBinding
import com.example.mafia.dialogs.JoinDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CountDownLatch

class MultiplayerMenu : AppCompatActivity(), JoinDialogFragment.JoinDialogListener {
    private lateinit var binding: ActivityMultiplayerMenuBinding
    private lateinit var nick: String
    private lateinit var database: FirebaseDatabase
    private lateinit var roomRef: DatabaseReference
    private var roomId: String = ""
    private val roomIdLatch = CountDownLatch(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        nick = preferences.getString(Preferences.USERNAME_TAG, null).toString()


        binding.buttonJoinGame.setOnClickListener {
            val joinDialog = JoinDialogFragment()
            joinDialog.show(supportFragmentManager, "JoinDialog")
        }

        roomRef = FirebaseDatabase.getInstance().getReference("rooms")
        Thread {
            roomIdLatch.await()
            runOnUiThread {
                checkGameStatus()
            }
        }.start()
    }
    override fun sendCode(code: String) {
        val roomRef = database.reference.child("rooms").child(code).child("players")
        roomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    roomId = code
                    roomIdLatch.countDown()
                    val player = Player("", nick, Player.Role.Unknown, true, "")
                    val playerId = roomRef.push().key ?: ""
                    player.playerId = playerId
                    roomRef.child(playerId).setValue(player)
                    Toast.makeText(applicationContext, "Вы зашли в комнату!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Ошибка, если код комнаты неверный
                    Toast.makeText(applicationContext, "Неверный код комнаты", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkGameStatus() {
        roomRef.child(roomId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val gameStatus = dataSnapshot.child("gameStatus").value
                    val isGameStarted = gameStatus as? Boolean
                    Log.i(TAG, "gameStatus равен: $isGameStarted")
                    if (isGameStarted != null && isGameStarted) {
                        // Комната существует и игра уже началась
                        startOtherActivity()
                    } else {
                        //вызываем функцию до тех пор, пока не изменится gameStatus
                        checkGameStatus()
                        // Игра еще не началась
                        // Действия, которые нужно выполнить, если игра не началась
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@MultiplayerMenu,
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Проверяем статус игры при возобновлении активности
        checkGameStatus()
    }

    private fun startOtherActivity() {
        val intent = Intent(this, DistributionRoles::class.java)
        startActivity(intent)
    }

    private fun showJoinDialog() {
        val joinDialog = JoinDialogFragment()
        joinDialog.show(supportFragmentManager, "JoinDialog")
    }

    fun onClickButtonCreateGame(view: View) {
        val intent = Intent(this, MultiplayerRoom::class.java)
        startActivity(intent)
    }

    fun onClickButtonJoinGame(view: View) {
        showJoinDialog()
    }


}