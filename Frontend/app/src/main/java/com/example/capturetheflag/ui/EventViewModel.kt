package com.example.capturetheflag.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.EventDetailsModel
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.models.StatusModel
import com.example.capturetheflag.sharedprefrences.userPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel(app:Application):AndroidViewModel(app) {
    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel>?{
        return eventResposeLiveData!!
    }
    private val session = userPreferences.getInstance(app.applicationContext)
    private val id = session.getUID()

    fun eventDetails(eid:Int, callback: (EventDetailsModel?,Boolean?) -> Unit){
        val call = RetrofitInstances.service
        call.getEventDetails(id,eid).enqueue(object: Callback<EventDetailsModel>{
            override fun onResponse(
                call: Call<EventDetailsModel>,
                response: Response<EventDetailsModel>
            ) {
                response.body()?.let {
                    callback(it,false)
                }
            }
            override fun onFailure(call: Call<EventDetailsModel>, t: Throwable) {
                callback(null,true)
            }

        })
    }
    fun getAdminEventbyId(eid:Int){
        Log.w("sebastian evm",eid.toString())
            RetrofitInstances.service.getEventbyId(eid)
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        Log.w("sebastian evm", response.body().toString())

                        eventResposeLiveData.value = response.body()
                        Log.w("Sebastian", eventResposeLiveData.value.toString())

                    }

                    override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                        Log.d("TAG", t.message.toString())
                    }
                })

    }
    fun registerUserForEvent(eid:Int){
            RetrofitInstances.service.registerUserForRegistration(id,eid)
                .enqueue(object: Callback<StatusModel>{
                    override fun onResponse(
                        call: Call<StatusModel>,
                        response: Response<StatusModel>
                    ) {
                        Log.e("register user", response.body().toString())
                    }

                    override fun onFailure(call: Call<StatusModel>, t: Throwable) {
                        Log.e("NetworkError",t.toString())

                    }

                })
    }
}