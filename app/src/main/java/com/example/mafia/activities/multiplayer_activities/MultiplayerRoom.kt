package com.example.mafia.activities.multiplayer_activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mafia.GameCycle
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityMultiplayerRoomHostBinding
import com.example.mafia.databinding.ActivityMultiplayerRoomPlayerBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlin.random.Random
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MultiplayerRoom : AppCompatActivity() {
    private lateinit var bindingHostLayout: ActivityMultiplayerRoomHostBinding
    private lateinit var bindingPlayerLayout: ActivityMultiplayerRoomPlayerBinding
    private lateinit var roomId: String
    private lateinit var currentPlayerId: String
    private lateinit var creatorPlayer: Player
    private lateinit var database: FirebaseDatabase
    private lateinit var playersRef: DatabaseReference
    private lateinit var gameStatusRef: DatabaseReference
    private lateinit var roundNumberRef: DatabaseReference
    private lateinit var currentPlayerIdRef: DatabaseReference
    private lateinit var roomRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (intent.getStringExtra(Preferences.PROPERTY_TAG)) {
            Preferences.PLAYER_TAG -> {
                bindingPlayerLayout = ActivityMultiplayerRoomPlayerBinding.inflate(layoutInflater)
                setContentView(bindingPlayerLayout.root)
                val roomCode = intent.getStringExtra(Preferences.ROOM_CODE)
                val playerId = intent.getStringExtra("playerId")
                bindingPlayerLayout.textViewRoomCode.text = roomCode
            }
            Preferences.HOST_TAG -> {
                bindingHostLayout = ActivityMultiplayerRoomHostBinding.inflate(layoutInflater)
                setContentView(bindingHostLayout.root)

                // Генерируем уникальный идентификатор комнаты
                roomId = generateRoomId()
                bindingHostLayout.textViewRoomCode.text = roomId
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
                val username = preferences.getString(Preferences.USERNAME_TAG, null).toString()

                // Create the player object for the creator
                creatorPlayer = Player("", username, Player.Role.Unknown.toString(), true, "")

                // Add the creator player to the players collection
                val creatorPlayerId = roomRef.child("players").push().key ?: ""
                creatorPlayer.playerId = creatorPlayerId
                currentPlayerId = creatorPlayerId
                roomRef.child("players").child(creatorPlayerId).setValue(creatorPlayer)

                bindingHostLayout.buttonStartGame.setOnClickListener {
                    // Проверяем количество игроков в комнате
                    playersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val playersCount = dataSnapshot.childrenCount.toInt()

                            if (playersCount >= 2) {
                                // Количество игроков достаточно, запускаем игру
                                gameStatusRef.setValue(true)
                                roundNumberRef.setValue(1)
                                currentPlayerIdRef.setValue(currentPlayerId)

                                distributeRolesFromFirebase(roomId) //распределение ролей
                                // переход на окно распределение ролей
                                val intent = Intent(applicationContext, GameCycle::class.java)
                                intent.putExtra("roomId", roomId)
                                startActivity(intent)
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
        }
    }
    private fun distributeRolesFromFirebase(roomCode: String) {
        val playersRef = database.reference.child("rooms").child(roomCode).child("players")

        playersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val playerList: MutableList<Player> = mutableListOf()

                for (playerSnapshot in dataSnapshot.children) {
                    val playerId = playerSnapshot.key ?: ""
                    val playerName = playerSnapshot.child("username").value as String

                    // Создайте экземпляр класса Player и добавьте его в список игроков
                    val player = Player(playerId, playerName, Player.Role.Unknown.toString(), true, "")
                    playerList.add(player)
                }

                // Распределите роли между игроками
                distributeRoles(playerList)

                // Сохраните обновленные данные о ролях игроков в базе данных Firebase
                saveRolesToFirebase(playerList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки получения данных из Firebase
            }
        })
    }
    private fun distributeRoles(playerList: MutableList<Player>) {
        val numPlayers = playerList.size
        val numMafia = numPlayers / 4 // Количество мафий

        val availableRoles = mutableListOf<Player.Role>()
        repeat(numMafia) {
            availableRoles.add(Player.Role.Mafia)
        }
        repeat(numPlayers - numMafia) {
            availableRoles.add(Player.Role.Civilian)
        }

        playerList.shuffle() // Перемешиваем список игроков

        for (i in 0 until numPlayers) {
            val player = playerList[i]
            val roleIndex = (0 until availableRoles.size).random()
            val role = availableRoles[roleIndex]
            availableRoles.removeAt(roleIndex)

            player.role = role.toString()
        }
    }
    private fun saveRolesToFirebase(playerList: List<Player>) {
        for (player in playerList) {
            val playerRef = database.reference.child("rooms").child(roomId).child("players").child(player.playerId)
            playerRef.child("role").setValue(player.role)
        }
    }

    private fun generateRoomId(): String {
        val charPool: List<Char> = ('A'..'Z').toList()
        return (1..4)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }
}