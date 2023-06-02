package com.example.mafia.activities.multiplayer_activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mafia.Player
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
        playersRef = database.reference.child("rooms").child(roomId).child("players")
        gameStatusRef = database.reference.child("rooms").child(roomId).child("gameStatus")
        roundNumberRef = database.reference.child("rooms").child(roomId).child("roundNumber")
        currentPlayerIdRef = database.reference.child("rooms").child(roomId).child("currentPlayerId")

        // Получение ника игрока
        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        val nick = preferences.getString(Preferences.USERNAME_TAG, null).toString()

        // Создание объекта текущего игрока
        val creatorPlayer = Player("", nick, Player.Role.Unknown, true, "")

        // Add the creator player to the players collection
        val creatorPlayerId = playersRef.push().key ?: ""
        creatorPlayer.playerId = creatorPlayerId
        playersRef.child(creatorPlayerId).setValue(creatorPlayer)

        currentPlayerId = creatorPlayerId

        buttonStartGame = binding.buttonStartGame
        buttonStartGame.isEnabled = false

        // Set up a listener to check the number of players
        val playersCountListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val playersCount = dataSnapshot.childrenCount.toInt()

                buttonStartGame.isEnabled = playersCount >= 4
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        }

        playersRef.addValueEventListener(playersCountListener)

        buttonStartGame.setOnClickListener {
            // Start the game by updating the gameStatus and roundNumber
            gameStatusRef.setValue("active")
            roundNumberRef.setValue(1)
            currentPlayerIdRef.setValue(currentPlayerId)

            // Perform necessary actions to start the game
        }
    }

    /*private fun distributeRoles() {
        playersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val players: MutableList<String> = mutableListOf()

                for (playerSnapshot in dataSnapshot.children) {
                    val player = playerSnapshot.getValue(Player::class.java)
                    player?.let {
                        players.add(it.nickname)
                    }
                }

                val roles: MutableList<String> = mutableListOf(
                    "Mafia",
                    "Mafia",
                    "Civilian",
                    "Civilian",
                    "Civilian",
                    "Civilian",
                    "Sheriff",
                    "Doctor"
                )

                // Перемешиваем роли
                roles.shuffle()

                // Распределяем роли между игроками
                val rolesMap: MutableMap<String, String> = mutableMapOf()
                for (player in players) {
                    val role = roles.removeAt(0)
                    rolesMap[player] = role
                }

                // Сохраняем распределение ролей в базе данных
                databaseReference.child("rooms").child(roomId).child("roles").setValue(rolesMap)

                // TODO: Выполните действия, связанные с началом игры
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки, если такая имеется
            }
        })
    }*/
    private fun generateRoomId(): String {
        val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
        return (1..4)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }
    /*private fun checkPlayersCount() {
        playersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val playersCount = dataSnapshot.childrenCount.toInt()

                if (playersCount <= 4) {
                    Toast.makeText(applicationContext, "Должно быть как минимум 4 игрока", Toast.LENGTH_SHORT).show()
                } else {
                    // Распределение ролей
                    distributeRoles()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки, если такая имеется
            }
        })
    }

    private fun distributeRoles() {
        playersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val players: MutableList<String> = mutableListOf()

                for (playerSnapshot in dataSnapshot.children) {
                    val player = playerSnapshot.getValue(Player::class.java)
                    player?.let {
                        players.add(it.nickname)
                    }
                }

                val roles: MutableList<String> = mutableListOf(
                    "Мафия",
                    "Мафия",
                    "Мирный житель",
                    "Мирный житель",
                    "Мирный житель",
                    "Мирный житель",
                    "Шериф",
                    "Доктор"
                )

                // Перемешиваем роли
                roles.shuffle()

                // Распределяем роли между игроками
                val rolesMap: MutableMap<String, String> = mutableMapOf()
                for (player in players) {
                    val role = roles.removeAt(0)
                    rolesMap[player] = role
                }

                // Сохраняем распределение ролей в базе данных
                databaseReference.child("rooms").child(roomId).child("roles").setValue(rolesMap)

                // TODO: Выполните действия, связанные с началом игры
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки, если такая имеется
            }
        })
    }*/
}