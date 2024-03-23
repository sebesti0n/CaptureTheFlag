package com.tejasdev.repospect.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tejasdev.repospect.apiServices.RetrofitInstances
import com.tejasdev.repospect.models.ResponseEventModel
import com.tejasdev.repospect.session.Session
import com.tejasdev.repospect.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminHomeViewModel(
    private val app:Application
): AndroidViewModel(app) {

    var eventResponseLiveData= MutableLiveData<Resource<ResponseEventModel>>()

    private val session = Session.getInstance(app.applicationContext)
    private val id = session.getUID()
    fun getAdminEvents() {
        eventResponseLiveData.postValue(Resource.Loading())
            RetrofitInstances.service.getEvent(id).enqueue(object : Callback<ResponseEventModel> {
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
                    else eventResponseLiveData.postValue(
                        Resource.Error(
                            response.message()
                        )
                    )
                    Log.w("Sebastian", eventResponseLiveData.value.toString())
                }

                override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                    eventResponseLiveData.postValue(
                        Resource.Error(
                            t.message
                        )
                    )
                }
            })
        }

}