package com.sebesti0n.capturetheflag.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sebesti0n.capturetheflag.R
import com.sebesti0n.capturetheflag.models.QuestionModel

class QuestionAdapter(private val listner: QuestionItemClickListener): RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    private var list:ArrayList<QuestionModel> = ArrayList()
    class QuestionViewHolder(view:View):RecyclerView.ViewHolder(view) {
       val qItem = view.findViewById<TextView>(R.id.question)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.question_itemview,null))
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val currQues = list[position]
        val ques = "Q${position+1} - ${currQues.question}?"
        holder.qItem.setText(ques)
        holder.itemView.setOnClickListener {
            listner.onQuestionClickListner(currQues)
        }
    }
    fun addData(ques:QuestionModel){
        list.add(ques);
        notifyDataSetChanged()
    }
    fun setData(list:ArrayList<QuestionModel>){
        this.list=list
        notifyDataSetChanged()
    }

}