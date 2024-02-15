package com.example.capturetheflag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capturetheflag.models.RiddleModel
import com.example.capturetheflag.session.CtfSession
import com.github.vipulasri.timelineview.TimelineView

class TimeLineAdapter: RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private var list: List<RiddleModel> = listOf()
    private var session: CtfSession? = null

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

    private fun currentQuestion(): Int = session!!.getLevel()

    fun createSession(newSession: CtfSession) {
        session = newSession
        notifyDataSetChanged()
    }

    fun setData(newList: List<RiddleModel>){
        list = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val item = list[position]
        val res = holder.itemView.context.resources
        val itemType = TimelineView.getTimeLineViewType(position, itemCount)
        when{
            position<currentQuestion() -> {
                holder.apply {
                    timeline.marker = res.getDrawable(R.drawable.done_vector_marker)
                    timeline.setEndLineColor(
                        res.getColor(R.color.blue_light),
                        itemType
                    )
                    timeline.setStartLineColor(
                        res.getColor(R.color.blue_light),
                        itemType
                    )
                    title.text = "Submitted Successfully"
                    desc.visibility = View.GONE
                }
            }
            position==currentQuestion() ->{
                holder.apply {
                    timeline.marker = res.getDrawable(R.drawable.current_riddle_marker)
                    timeline.setStartLineColor(
                        res.getColor(R.color.blue_light),
                        itemType
                    )
                    timeline.setEndLineColor(
                        res.getColor(R.color.text_color),
                        itemType
                    )
                    title.text = item.question_id.toString()
                    desc.text = item.question
                }
            }
            else -> {
                holder.apply {
                    timeline.marker = res.getDrawable(R.drawable.upcoming_vector_marker)
                    timeline.setStartLineColor(
                        res.getColor(R.color.text_color),
                        TimelineView.getTimeLineViewType(position, itemCount)
                    )
                    timeline.setEndLineColor(
                        res.getColor(R.color.text_color),
                        TimelineView.getTimeLineViewType(position, itemCount)
                    )
                    title.text = "Upcoming"
                    desc.visibility = View.GONE
                }
            }
        }
        holder.desc.text = "This is riddle $item"
        holder.title.text = "Riddle $item"
    }
}