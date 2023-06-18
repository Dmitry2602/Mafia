package com.example.mafia.activities.multiplayer_activities

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mafia.Preferences
import com.example.mafia.R
import com.example.mafia.VotingAdapter
import com.example.mafia.databinding.ActivityGameCycleDistributionRolesBinding
import com.example.mafia.databinding.ActivityGameCycleNightBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CountDownLatch

class GameCycleMultiplayer : AppCompatActivity() {
    private lateinit var bindingDistributionRoles: ActivityGameCycleDistributionRolesBinding
    private lateinit var bindingNight: ActivityGameCycleNightBinding
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
        bindingDistributionRoles = ActivityGameCycleDistributionRolesBinding.inflate(layoutInflater)
        bindingNight = ActivityGameCycleNightBinding.inflate(layoutInflater)

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


        setContentView(bindingDistributionRoles.root)

//Я сократил время таймера для тестов
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                bindingDistributionRoles.buttonTimer.text = (millisUntilFinished / 1000).toString()
            }

//Здесь можешь работать с голосованием
            override fun onFinish() {
                setContentView(bindingNight.root)
                val manager = LinearLayoutManager(applicationContext)
                //Ещё часть кода (обработка нажатия кнопок, кол-во проголосовавших) смотреть в VotingAdapter.kt
                val adapter = VotingAdapter()
                //Здесь заносишь игроков для голосования через MutableList
                adapter.playersList = mutableListOf("dmi3chh", "Dm1trofan", "visokaya", "nikonova_dn")
                    /*
                     *  Допустим, у тебя есть переменная playersInRoom <List<Player>>, которая хранит информацию о всех игроках в комнате.
                     *  Подсоси оттуда всех игроков, role которых Mafia и alive true. Передай этот MutableList в playersList.
                     */

                bindingNight.recyclerViewVoting.layoutManager = manager
                bindingNight.recyclerViewVoting.adapter = adapter
            }
        }.start()
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
                        bindingDistributionRoles.imageButtonRole.setImageResource(R.drawable.role_mafia)
                        bindingDistributionRoles.imageButtonRole.visibility = View.VISIBLE
                        bindingDistributionRoles.textViewRole.text = "Вы Мафия"
                    }
                    if (role == Player.Role.Civilian.toString()){
                        // Отображаем роль игрока в textViewRole
                        bindingDistributionRoles.imageButtonRole.setImageResource(R.drawable.role_citizens)
                        bindingDistributionRoles.imageButtonRole.visibility = View.VISIBLE
                        bindingDistributionRoles.textViewRole.text = "Вы мирный житель"
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