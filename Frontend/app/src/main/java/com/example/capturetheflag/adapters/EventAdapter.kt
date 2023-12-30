package com.example.capturetheflag.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capturetheflag.R
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.util.EventItemClickListner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventAdapter(private val listner: EventItemClickListner):
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

        private var list :ArrayList<Event> = arrayListOf()

        class EventViewHolder(view:View):RecyclerView.ViewHolder(view){
            val title = view.findViewById<TextView>(R.id.heading)
            val organiser= view.findViewById<TextView>(R.id.org_company)
            val location = view.findViewById<TextView>(R.id.location)
            val date = view.findViewById<TextView>(R.id.event_date)
            val month =  view.findViewById<TextView>(R.id.event_month)
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currEvent = list[position]
        val dayandDate = getDayandMonth(currEvent.start_time)
        holder.title.text = currEvent.title
        holder.date.text =dayandDate.first
        holder.month.text =dayandDate.second
        holder.location.text = currEvent.location
        holder.organiser.text = currEvent.organisation
        holder.itemView.setOnClickListener {
            listner.onEventClickListner(currEvent)
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(list:ArrayList<Event>){
        this.list=list
        notifyDataSetChanged()
    }
    fun getDayandMonth(timestamp:String):Pair<String,String>{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date: Date = inputFormat.parse(timestamp) ?: Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val monthNumber = calendar.get(Calendar.MONTH)
            val monthAbbreviations = arrayOf(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )
            val monthAbbreviation = monthAbbreviations[monthNumber]
        return Pair(day.toString(),monthAbbreviation)
    }

}