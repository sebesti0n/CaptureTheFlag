package com.tejasdev.repospect.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tejasdev.repospect.apiServices.RetrofitInstances
import com.tejasdev.repospect.models.EventX
import com.tejasdev.repospect.models.QuestionModel
import com.tejasdev.repospect.models.ResponseEventModel
import com.tejasdev.repospect.models.taskResponseModel
import com.tejasdev.repospect.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.tejasdev.repospect.util.Resource

class CreateEventViewModel(
    private val app:Application
): AndroidViewModel(app) {

    private var eventResposeLiveData= MutableLiveData<Resource<ResponseEventModel>>()
    fun get(): LiveData<Resource<ResponseEventModel>>?{
        return eventResposeLiveData!!
    }
    private val session = Session.getInstance(app.applicationContext)
    fun getUID():Int = session.getUID()

    fun createEvent(event: EventX){
        eventResposeLiveData.postValue(Resource.Loading())
            RetrofitInstances.service.createEvent(event)
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        if(response.isSuccessful){
                            response.body()?.let {
                                eventResposeLiveData.postValue(Resource.Success(it))
                            }
                        }
                        else eventResposeLiveData.postValue(Resource.Error(response.message()))
                    }

                    override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                        Log.d("TAG", t.message.toString())
                        eventResposeLiveData.postValue(Resource.Error(t.message))
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