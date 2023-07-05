package com.example.mafia.activities.local_activities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.RecyclerViewVoteBinding

class VotingAdapterLocal : RecyclerView.Adapter<VotingAdapterLocal.VoteViewHolder>() {
    var playersList: ArrayList<PlayerParcelable> = arrayListOf()
    private lateinit var listener: RecyclerViewRevotedListener

    interface RecyclerViewRevotedListener {
        fun restoreRecyclerView()
    }

    inner class VoteViewHolder(val binding: RecyclerViewVoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = playersList.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        listener = recyclerView.context as RecyclerViewRevotedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewVoteBinding.inflate(inflater, parent, false)

        return VoteViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        holder.binding.buttonVote.text = playersList[position].username

        holder.binding.imageViewVotes.text = (playersList[position].gotVotes).toString()
        if (playersList[position].gotVotes > 0) {
            holder.binding.imageViewVotes.visibility = View.VISIBLE
        } else
            holder.binding.imageViewVotes.visibility = View.INVISIBLE

        holder.binding.buttonVote.setOnClickListener {
            listener.restoreRecyclerView()
            holder.binding.buttonVote.isEnabled = false
            holder.binding.imageViewVotes.visibility = View.VISIBLE
            holder.binding.imageViewVotes.text = (playersList[position].gotVotes + 1).toString()
        }
    }
}