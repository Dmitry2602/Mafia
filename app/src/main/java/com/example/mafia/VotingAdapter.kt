package com.example.mafia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.RecyclerViewVoteBinding

class VotingAdapter : RecyclerView.Adapter<VotingAdapter.VoteViewHolder>() {
    //Это список, переданных игроков
    var playersList: MutableList<String> = mutableListOf()
    //ЭТО НЕ ТРОГАЙ
    inner class VoteViewHolder(val binding: RecyclerViewVoteBinding) :
        RecyclerView.ViewHolder(binding.root)
    //Это количество игроков, переданное сюда
    override fun getItemCount(): Int = playersList.size
    //ЭТО НЕ ТРОГАЙ (или можешь потрогать, если понадобится)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewVoteBinding.inflate(inflater, parent, false)

        //Здесь прячем кол-во проголосовавших
        binding.imageViewVotes.visibility = View.INVISIBLE

        return VoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        val player = playersList[position]
        val context = holder.itemView.context
        //Здесь задаём текст для каждого игрока
        holder.binding.buttonVote.text = playersList[position]
        //Здесь можно обработать кнопку нажатия на игрока
        holder.binding.buttonVote.setOnClickListener {  }
    }
}