package com.example.mafia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.RecyclerViewPlayerBinding

class PlayerAdapter : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    var playersList: MutableList<String?> = mutableListOf()
    private lateinit var listener: RecyclerViewChangedListener

    interface RecyclerViewChangedListener {
        fun sendItemCount(itemCount: Int)
    }

    inner class PlayerViewHolder(val binding: RecyclerViewPlayerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = playersList.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        listener = recyclerView.context as RecyclerViewChangedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewPlayerBinding.inflate(inflater, parent, false)

        listener.sendItemCount(itemCount)
        binding.editTextPlayer.requestFocus()

        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.binding.editTextPlayer.setText(playersList[position])

        holder.binding.editTextPlayer.doOnTextChanged { text, _, _, _ ->
            playersList[position] = text.toString()
        }

        holder.binding.imageButtonDeletePlayer.setOnClickListener {
            playersList.removeAt(position)
            listener.sendItemCount(itemCount)
            notifyItemRemoved(position)
        }
    }
}