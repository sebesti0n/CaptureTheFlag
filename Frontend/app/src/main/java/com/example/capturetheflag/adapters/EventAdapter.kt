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

class EventAdapter(private val context:Context,private val listner: EventItemClickListner):
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

        private var list :ArrayList<EventX> = arrayListOf()

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
    ): EventAdapter.EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,null))
    }

    override fun onBindViewHolder(holder: EventAdapter.EventViewHolder, position: Int) {
        val currEvent = list[position]
        holder.title.text = currEvent.title
        holder.date.text = "27"
        holder.month.text = "Dec"
        holder.location.text = currEvent.location
        holder.organiser.text = currEvent.organisation
        holder.itemView.setOnClickListener {
            listner.onEventClickListner()
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(list:ArrayList<EventX>){
        this.list=list
        notifyDataSetChanged()
    }

}