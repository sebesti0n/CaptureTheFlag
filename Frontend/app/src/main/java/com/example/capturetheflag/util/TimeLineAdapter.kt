package com.example.capturetheflag.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capturetheflag.R
import com.example.capturetheflag.models.RiddleModel
import com.github.vipulasri.timelineview.TimelineView

class TimeLineAdapter: RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private var list: List<RiddleModel> = listOf()
    private var level: Int = 0

    inner class TimeLineViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.riddle_title)
        val desc = view.findViewById<TextView>(R.id.riddle_desc)
        val timeline = view.findViewById<TimelineView>(R.id.timeline)
        init{
            timeline.initLine(viewType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.event_riddle_item_view,
                    null
                ),
            viewType
        )
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun getItemCount(): Int {
        return level+1
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val item = list[position]
        val res = holder.itemView.context.resources
        val itemType = TimelineView.getTimeLineViewType(position, itemCount)
            //
        holder.desc.text = "This is riddle $item"
        holder.title.text = "Riddle $item"
    }

    fun setData(newList: List<RiddleModel>, level: Int){
        this.level = level
        list = newList
        notifyDataSetChanged()
    }
}
