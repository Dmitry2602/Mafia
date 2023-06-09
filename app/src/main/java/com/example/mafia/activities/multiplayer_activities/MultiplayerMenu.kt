package com.example.mafia.activities.multiplayer_activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mafia.GameCycle
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
    private lateinit var username: String
    private lateinit var database: FirebaseDatabase
    private lateinit var roomRef: DatabaseReference
    private lateinit var player: Player
    private var roomId: String = ""
    private val roomIdLatch = CountDownLatch(1)
    private var isGameCycleStarted = false // Флаг для отслеживания статуса перехода на GameCycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        username = preferences.getString(Preferences.USERNAME_TAG, null).toString()

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
                    player = Player("", username, Player.Role.Unknown.toString(), true, "")
                    val playerId = roomRef.push().key ?: ""
                    player.playerId = playerId
                    roomRef.child(playerId).setValue(player)
                    Toast.makeText(applicationContext, "Вы зашли в комнату!", Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(applicationContext, MultiplayerRoom::class.java)
                    intent.putExtra(Preferences.PROPERTY_TAG, Preferences.PLAYER_TAG)
                    intent.putExtra(Preferences.ROOM_CODE, roomId)
                    intent.putExtra("playerId", player.playerId)
                    startActivity(intent)

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
        if (isGameCycleStarted) {
            return // Остановка рекурсии, если уже был переход на GameCycle активити
        }

        roomRef.child(roomId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val gameStatus = dataSnapshot.child("gameStatus").value
                    val isGameStarted = gameStatus as? Boolean
                    Log.i(TAG, "gameStatus равен: $isGameStarted")
                    if (isGameStarted != null && isGameStarted) {
                        // Комната существует и игра уже началась
                        isGameCycleStarted = true // Установка флага, что переход на GameCycle активити уже был
                        val intent = Intent(applicationContext, GameCycle::class.java)
                        intent.putExtra("roomId", roomId)
                        startActivity(intent)
                    } else {
                        if (isGameCycleStarted) {
                            return // Остановка рекурсии, если уже был переход на GameCycle активити
                        }
                        // Вызываем функцию до тех пор, пока не изменится gameStatus
                        checkGameStatus()
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

    fun onClickButtonJoinGame(view: View) {
        val joinDialog = JoinDialogFragment()
        joinDialog.show(supportFragmentManager, Preferences.DIALOG_TAG_JOIN)
    }

    fun onClickButtonCreateGame(view: View) {
        val intent = Intent(this, MultiplayerRoom::class.java)
        intent.putExtra(Preferences.PROPERTY_TAG, Preferences.HOST_TAG)
        startActivity(intent)
    }
}