package com.example.capturetheflag.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.models.taskResponseModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEventViewModel: ViewModel() {
    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel>?{
        return eventResposeLiveData!!
    }


    fun createEvent(event: EventX){
        viewModelScope.launch {
            RetrofitInstances.service.createEvent(event)
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        eventResposeLiveData.value = response.body()
                    }

                    override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                        Log.d("TAG", t.message.toString())
                    }

                })
        }
    }
    fun addTasks(tList: ArrayList<QuestionModel>) {
        viewModelScope.launch {
            RetrofitInstances.service.addTasks(tList).enqueue(object : Callback<taskResponseModel> {
                override fun onResponse(
                    call: Call<taskResponseModel>,
                    response: Response<taskResponseModel>
                ) {
//                Toast.makeText(,"hii",Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<taskResponseModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}