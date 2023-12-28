package com.example.capturetheflag.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capturetheflag.R
import com.example.capturetheflag.models.QuestionModel

class QuestionAdapter(private val list:ArrayList<QuestionModel>,private val listner: QuestionItemClickListner): RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
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


}