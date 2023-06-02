package com.example.mafia.activities.multiplayer_activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.mafia.Player
import com.example.mafia.Preferences
import com.example.mafia.databinding.ActivityMultiplayerMenuBinding
import com.example.mafia.dialogs.JoinDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MultiplayerMenu : AppCompatActivity(), JoinDialogFragment.JoinDialogListener {
    private lateinit var buttonStartGame: Button
    private lateinit var binding: ActivityMultiplayerMenuBinding
    private lateinit var roomId: String
    private lateinit var currentPlayerId: String
    private lateinit var nick: String

    private lateinit var database: FirebaseDatabase
    private lateinit var playersRef: DatabaseReference
    private lateinit var gameStatusRef: DatabaseReference
    private lateinit var roundNumberRef: DatabaseReference
    private lateinit var currentPlayerIdRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getStringExtra("roomCode") ?: ""

        // Инициализация базы данных и ссылки Firebase
        database = FirebaseDatabase.getInstance()
        playersRef = database.reference.child("rooms").child(roomId).child("players")
        gameStatusRef = database.reference.child("rooms").child(roomId).child("gameStatus")
        roundNumberRef = database.reference.child("rooms").child(roomId).child("roundNumber")
        currentPlayerIdRef = database.reference.child("rooms").child(roomId).child("currentPlayerId")


        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        nick = preferences.getString(Preferences.USERNAME_TAG, null).toString()

        // создание класса игрока
        val creatorPlayer = Player("", nick, Player.Role.Unknown, true, "")

        val creatorPlayerId = playersRef.push().key ?: ""
        creatorPlayer.playerId = creatorPlayerId
        playersRef.child(creatorPlayerId).setValue(creatorPlayer)

        currentPlayerId = creatorPlayerId

        buttonStartGame = binding.buttonStartGame

        buttonStartGame.setOnClickListener {
            val joinDialog = JoinDialogFragment()
            joinDialog.show(supportFragmentManager, "JoinDialog")
        }
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

    override fun sendCode(code: String) {
        val enteredCode = code
        val roomRef = database.reference.child("rooms").child(enteredCode).child("players")

        roomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // подключение игрока в комнату, если код верный
                    val player = Player("", nick, Player.Role.Unknown, true, "")
                    val playerId = roomRef.push().key ?: ""
                    player.playerId = playerId
                    roomRef.child(playerId).setValue(player)
                    Toast.makeText(applicationContext, "Player added to the room", Toast.LENGTH_SHORT).show()
                } else {
                    // ошибка, если код неверный
                    Toast.makeText(applicationContext, "Invalid room code", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}