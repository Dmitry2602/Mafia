package com.example.mafia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.RecyclerViewPlayerBinding

class PlayerAdapter : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    var playersList: MutableList<String?> = mutableListOf()

    inner class PlayerViewHolder(val binding: RecyclerViewPlayerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = playersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewPlayerBinding.inflate(inflater, parent, false)

        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = playersList[position]
        val context = holder.itemView.context

        if (itemCount <= 4)
            holder.binding.imageButtonDeletePlayer.visibility = View.GONE

        holder.binding.editTextPlayer.doOnTextChanged { text, _, _, _ ->
            playersList[position] = text.toString()
        }

        holder.binding.imageButtonDeletePlayer.setOnClickListener {
            playersList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }
}