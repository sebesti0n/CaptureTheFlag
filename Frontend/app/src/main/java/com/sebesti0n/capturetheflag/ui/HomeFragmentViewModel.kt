package com.sebesti0n.capturetheflag.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sebesti0n.capturetheflag.apiServices.RetrofitInstances
import com.sebesti0n.capturetheflag.models.ResponseEventModel
import com.sebesti0n.capturetheflag.session.Session
import com.sebesti0n.capturetheflag.util.Resource

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel(
    private val app:Application
): AndroidViewModel(app) {
    var eventResponseLiveData= MutableLiveData<Resource<ResponseEventModel>>()

    var liveEventResponseLiveData= MutableLiveData<Resource<ResponseEventModel>>()

    private val session = Session.getInstance(app.applicationContext)
    val enroll_id = session.getEnrollmentID()

    fun getUpcomingEvents() {
        eventResponseLiveData.postValue(Resource.Loading())
            RetrofitInstances.service.getAllEvent()
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        if(response.isSuccessful){
                            response.body()?.let{
                                eventResponseLiveData.postValue(
                                    Resource.Success(
                                        it
                                    )
                                )
                            }
                        }
                        else eventResponseLiveData.postValue(Resource.Error(response.message()))
                        Log.w("Sebastian viewModel", eventResponseLiveData.value.toString())

                    }

                    override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                        Log.d("TAG", t.message.toString())
                        eventResponseLiveData.postValue(
                            Resource.Error(
                                t.message
                            )
                        )
                    }
                })
    }
    fun getLiveEvents(){
        liveEventResponseLiveData.postValue(Resource.Loading())
            RetrofitInstances.service.getliveEvent().enqueue(object: Callback<ResponseEventModel> {
                override fun onResponse(
                    call: Call<ResponseEventModel>,
                    response: Response<ResponseEventModel>
                ){

                    if(response.isSuccessful){
                        response.body()?.let{
                            liveEventResponseLiveData.postValue(
                                Resource.Success(
                                    it
                                )
                            )
                        }
                    }
                    else liveEventResponseLiveData.postValue(Resource.Error(response.message()))
                    Log.w("Sebastian",eventResponseLiveData.value.toString())

                }
                override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                    liveEventResponseLiveData.postValue(Resource.Error(t.message))
                }
            })
        }
}