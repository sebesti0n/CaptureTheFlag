package com.example.capturetheflag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView

class TimeLineAdapter: RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private val list: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7)

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
                .inflate(R.layout.event_riddle_item_view, null),
            viewType
        )
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val item = list[position]
        val res = holder.itemView.context.resources
        val itemType = TimelineView.getTimeLineViewType(position, itemCount)
        when{
            item<=3 -> {
                holder.timeline.marker = res.getDrawable(R.drawable.done_vector_marker)
                holder.timeline.setEndLineColor(res.getColor(R.color.blue_light), itemType)
                holder.timeline.setStartLineColor(res.getColor(R.color.blue_light), itemType)
            }
            item==4 ->{
                holder.timeline.marker = res.getDrawable(R.drawable.current_riddle_marker)
                holder.timeline.setStartLineColor(res.getColor(R.color.blue_light), itemType)
                holder.timeline.setEndLineColor(res.getColor(R.color.text_color), itemType)
            }
            else -> {
                holder.timeline.marker = res.getDrawable(R.drawable.upcoming_vector_marker)
                holder.timeline.setStartLineColor(res.getColor(R.color.text_color), TimelineView.getTimeLineViewType(position, itemCount))
                holder.timeline.setEndLineColor(res.getColor(R.color.text_color), TimelineView.getTimeLineViewType(position, itemCount))
            }
        }
        holder.desc.text = "This is riddle $item"
        holder.title.text = "Riddle $item"
    }
}