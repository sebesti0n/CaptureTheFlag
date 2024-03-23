package com.tejasdev.repospect.util

import com.tejasdev.repospect.models.QuestionModel

interface QuestionItemClickListener {
    fun onQuestionClickListner(ques:QuestionModel)
}