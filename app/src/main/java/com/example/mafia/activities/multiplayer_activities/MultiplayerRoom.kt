package com.example.mafia.activities.multiplayer_activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.mafia.DistributionRoles
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityMultiplayerRoomBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlin.random.Random
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MultiplayerRoom : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerRoomBinding
    private lateinit var buttonStartGame: Button
    private lateinit var roomId: String
    private lateinit var currentPlayerId: String
    private lateinit var currentPlayer: Player
    private lateinit var database: FirebaseDatabase
    private lateinit var playersRef: DatabaseReference
    private lateinit var gameStatusRef: DatabaseReference
    private lateinit var roundNumberRef: DatabaseReference
    private lateinit var currentPlayerIdRef: DatabaseReference
    private lateinit var roomRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerRoomBinding.inflate(layoutInflater)
        buttonStartGame = binding.buttonStartGame
        setContentView(binding.root)


        // Генерируем уникальный идентификатор комнаты
        roomId = generateRoomId()
        binding.codeRoom.text = roomId
        // Initialize Firebase database and references
        database = FirebaseDatabase.getInstance()
        roomRef = database.reference.child("rooms").child(roomId)
        // Создаем gameStatus, roundNumber и currentPlayerId со значениями по умолчанию
        roomRef.child("gameStatus").setValue(false)
        roomRef.child("roundNumber").setValue(0)
        roomRef.child("currentPlayerId").setValue("")
        playersRef = database.reference.child("rooms").child(roomId).child("players")
        gameStatusRef = database.reference.child("rooms").child(roomId).child("gameStatus")
        roundNumberRef = database.reference.child("rooms").child(roomId).child("roundNumber")
        currentPlayerIdRef = database.reference.child("rooms").child(roomId).child("currentPlayerId")


        // Получение ника игрока
        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        val nick = preferences.getString(Preferences.USERNAME_TAG, null).toString()

        // Create the player object for the creator
        val creatorPlayer = Player("", nick, Player.Role.Unknown, true, "")

        // Add the creator player to the players collection
        val creatorPlayerId = roomRef.child("players").push().key ?: ""
        creatorPlayer.playerId = creatorPlayerId
        currentPlayerId = creatorPlayerId
        roomRef.child("players").child(creatorPlayerId).setValue(creatorPlayer)



        buttonStartGame = binding.buttonStartGame

        buttonStartGame.setOnClickListener {
            // Проверяем количество игроков в комнате
            playersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val playersCount = dataSnapshot.childrenCount.toInt()

                    if (playersCount >= 4) {
                        // Количество игроков достаточно, запускаем игру
                        gameStatusRef.setValue(true)
                        roundNumberRef.setValue(1)
                        currentPlayerIdRef.setValue(currentPlayerId)
                        // переход на окно распределение ролей
                        onDistributionRoles()
                        Toast.makeText(this@MultiplayerRoom, "Игра была создана", Toast.LENGTH_SHORT).show()
                    } else {
                        // Количество игроков недостаточно, выводим ошибку
                        Toast.makeText(this@MultiplayerRoom, "Слишком мало игроков для начала игры", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обрабатываем ошибку базы данных
                }
            })
        }


    }

    fun onDistributionRoles(){
        val intent = Intent(this, DistributionRoles::class.java)
        startActivity(intent)
    }

    private fun generateRoomId(): String {
        val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
        return (1..4)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }
}