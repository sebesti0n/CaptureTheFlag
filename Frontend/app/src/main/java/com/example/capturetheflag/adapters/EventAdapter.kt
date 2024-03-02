package com.example.capturetheflag.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capturetheflag.R
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.util.EventItemClickListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventAdapter(
    private val listner: EventItemClickListener
): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var list :ArrayList<Event> = arrayListOf()

    class EventViewHolder(view:View):RecyclerView.ViewHolder(view){
        val image: ShapeableImageView = view.findViewById(R.id.event_image)
        val headingText: TextView = view.findViewById(R.id.titleTv)
        val locationText: TextView = view.findViewById(R.id.location_tv)
        val organisationText: TextView = view.findViewById(R.id.organisation_tv)
        val learnMoreBtn: MaterialButton = view.findViewById(R.id.btn_learn_more)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currEvent = list[position]
//        val dayandDate = getDayandMonth(currEvent.start_time)

        Glide.with(holder.itemView.context)
            .load(currEvent.posterImage)
            .into(holder.image)

        holder.headingText.text = currEvent.title
        holder.organisationText.text = currEvent.organisation
        holder.locationText.text = currEvent.location
        holder.learnMoreBtn.setOnClickListener {
            listner.onEventClickListner(currEvent)
        }
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

    fun setData(newList: List<Event>) {
        list = ArrayList(newList)
        notifyDataSetChanged()
    }

}