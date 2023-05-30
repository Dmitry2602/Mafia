package com.example.mafia.activities.multiplayer_activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerRoomBinding.inflate(layoutInflater)

        val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
        val code = (1..4)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
        binding.codeRoom.text = code

        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference = firebaseDatabase.reference
        // Берем ник игрока
        val preferences = getSharedPreferences(Preferences.TABLE_NAME, Context.MODE_PRIVATE)
        val nick = preferences.getString(Preferences.USERNAME_TAG, null).toString()

        /// Создание уникального идентификатора комнаты
        val roomId: String = code

        // Создание объекта игрока
        val playerData: HashMap<String, Any> = HashMap()
        playerData["nickname"] = nick

        // Сохранение игрока в комнату
        databaseReference.child("rooms").child(roomId).child("players").setValue(playerData)
        setContentView(binding.root)
        buttonStartGame = binding.buttonStartGame

        // Получение ссылки на коллекцию "players" в комнате
        val playersRef = databaseReference.child("rooms").child(roomId).child("players")

        buttonStartGame.setOnClickListener {
            val playersCountListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val playersCount = dataSnapshot.childrenCount.toInt()

                    if (playersCount < 4) {
                        val message = "Дождитесь пока присоединятся нужно количество игроков"
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        buttonStartGame.isEnabled = false
                    } else {
                        buttonStartGame.isEnabled = true
                        // Выполните нужные действия при достаточном количестве игроков для начала игры
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибки, если такая имеется
                }
            }

            playersRef.addListenerForSingleValueEvent(playersCountListener)
        }
    }
}