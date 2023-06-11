package com.example.mafia

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.example.mafia.activities.multiplayer_activities.Player
import com.example.mafia.databinding.ActivityGameCycleDistributionRolesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CountDownLatch

class GameCycle : AppCompatActivity() {
    private lateinit var binding: ActivityGameCycleDistributionRolesBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var roomId: String
    private lateinit var player: Player
    private lateinit var userPlayerId: String
    private lateinit var username: String
    private val roomIdLatch = CountDownLatch(1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = intent.getStringExtra("roomId").toString()
        Log.i(TAG, "РУМ АЙДИ: $roomId")
        binding = ActivityGameCycleDistributionRolesBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance()

        // Получение ника игрока
        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        username = preferences.getString(Preferences.USERNAME_TAG, null).toString()
        Log.i(TAG, "Username = $username")
        fetchPlayerIdByUsername(username) { playerId ->
            if (playerId != null) {
                userPlayerId = playerId
                Log.i(TAG, "playerId = $userPlayerId")

            } else {
                // Игрок с указанным ником не найден
            }
        }

        Thread {
            roomIdLatch.await()
            runOnUiThread {
                fetchPlayerData()
            }
        }.start()

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
    private fun fetchPlayerData() {
        val playerRef = database.reference.child("rooms").child(roomId).child("players").child(userPlayerId)
        Log.i(TAG, "зашли №1")
        playerRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val username = dataSnapshot.child("username").value as? String
                    val role = dataSnapshot.child("role").value as? String
                    Log.i(TAG, "username: $username")
                    Log.i(TAG, "role: $role")
                    if (role == Player.Role.Mafia.toString()){
                        // Отображаем роль игрока в textViewRole
                        binding.imageButtonRole.setImageResource(R.drawable.role_mafia)
                        binding.imageButtonRole.visibility = View.VISIBLE
                        binding.textViewRole.text = "Вы Мафия"
                    }
                    if (role == Player.Role.Civilian.toString()){
                        // Отображаем роль игрока в textViewRole
                        binding.imageButtonRole.setImageResource(R.drawable.citizens)
                        binding.imageButtonRole.visibility = View.VISIBLE
                        binding.textViewRole.text = "Вы мирный житель"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки получения данных
            }
        })
    }

    private fun fetchPlayerIdByUsername(username: String, callback: (String?) -> Unit) {
        val playersRef = database.reference.child("rooms").child(roomId).child("players")
        val query = playersRef.orderByChild("username").equalTo(username)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (playerSnapshot in dataSnapshot.children) {
                        val playerId = playerSnapshot.key
                        Log.i(TAG, "AAAAA: $playerId")
                        if (playerId != null) {
                            // playerId найден
                            roomIdLatch.countDown()
                            callback(playerId)
                            return
                        }
                    }
                }

                // Игрок с указанным ником не найден
                callback(null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки получения данных
                callback(null)
            }
        })
    }
}