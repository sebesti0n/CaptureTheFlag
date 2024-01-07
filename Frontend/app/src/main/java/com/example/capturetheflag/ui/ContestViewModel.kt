package com.example.capturetheflag.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.QuestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContestViewModel : ViewModel() {
    private var riddlesLivedata= MutableLiveData<ArrayList<QuestionModel>>()
    private var nextRiddleNo = MutableLiveData<Int?>()
    fun get():LiveData<ArrayList<QuestionModel>>{
        return riddlesLivedata
    }
    fun getNo():LiveData<Int?>{
        return nextRiddleNo
    }

    fun getRiddles(eid: Int, uid: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstances.service.getRiddles(eid, uid)
                if (response.isSuccessful && response.body() != null) {
                    Log.d("sebastian riddleList",response.body().toString())
                    withContext(Dispatchers.Main){
                        val rList = response.body()!!.questions
                        riddlesLivedata.postValue(rList)
                    }

                } else {
                    Log.d("sebastian riddleList", "Unsuccessful response or empty body")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("sebastian riddleList", "Exception occurred: ${e.message}")
            }
        }
    }
    fun getSubmissionDetails(eid: Int, uid: Int){
        viewModelScope.launch {
            try {
                val res = RetrofitInstances.service.getSubmissionDetails(eid, uid)
                if (res.isSuccessful && res.body()!=null){
                    Log.d("sebastian submissionDetails",res.body().toString())
                    val rNo = res.body()
                    if (rNo != null) {
                        nextRiddleNo.postValue(rNo.next)
                    }
                }else{
                    Log.d("sebastian riddleNo", "getting error while this")
                }
            }catch(e:Exception){
                e.printStackTrace()
                Log.d("sebastian riddleno", "Exception occurred: ${e.message}")
            }
        }
    }

}