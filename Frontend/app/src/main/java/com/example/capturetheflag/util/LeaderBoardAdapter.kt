package com.example.capturetheflag.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capturetheflag.R
import com.example.capturetheflag.models.LeaderBoardModel
import com.google.android.material.textview.MaterialTextView

class LeaderBoardAdapter:RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder>() {
private var list:ArrayList<LeaderBoardModel> = ArrayList()
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
        val tt= list[position].end_time-list[position].start_time
        holder.tv_position.text = (position+1).toString()
        holder.tv_teamName.text = list[position].name
        if(tt<0)
        holder.tv_timeTaken.text = "-"
        else
            holder.tv_timeTaken.text = tt.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
fun addList(list:ArrayList<LeaderBoardModel>){
    this.list=list;
    notifyDataSetChanged()
}
}