package com.example.capturetheflag.ui

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.models.taskResponseModel
import com.example.capturetheflag.sharedprefrences.userPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEventViewModel(
    private val app:Application
): AndroidViewModel(app) {

    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel>?{
        return eventResposeLiveData!!
    }
    private val session = userPreferences.getInstance(app.applicationContext)
    fun getUID():Int = session.getUID()

    fun createEvent(event: EventX){
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
    fun addTasks(tList: ArrayList<QuestionModel>) {
            RetrofitInstances.service.addTasks(tList).enqueue(object : Callback<taskResponseModel> {
                override fun onResponse(
                    call: Call<taskResponseModel>,
                    response: Response<taskResponseModel>
                ) {

                }

                override fun onFailure(call: Call<taskResponseModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

}