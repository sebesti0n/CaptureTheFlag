package com.example.capturetheflag.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capturetheflag.R
import com.google.android.material.textview.MaterialTextView

class LeaderBoardAdapter:RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder>() {

    class LeaderBoardViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tv_position  = view.findViewById<MaterialTextView>(R.id.position)
        val tv_teamName = view.findViewById<MaterialTextView>(R.id.team_name)
        val tv_timeTaken = view.findViewById<MaterialTextView>(R.id.timeTaken)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderBoardViewHolder{
        return LeaderBoardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.iteview_leaderboard,null))
    }

    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}