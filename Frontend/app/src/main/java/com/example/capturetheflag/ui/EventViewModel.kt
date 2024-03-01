package com.example.capturetheflag.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.EventsSchema
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.models.StatusModel
import com.example.capturetheflag.models.TeamSchema
import com.example.capturetheflag.session.Session
import com.example.capturetheflag.util.Resource

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel(app:Application):AndroidViewModel(app) {
    private var eventResponseLiveData= MutableLiveData<Resource<ResponseEventModel>>()
    fun get(): LiveData<Resource<ResponseEventModel>>?{
        return eventResponseLiveData!!
    }
    private val session = Session.getInstance(app.applicationContext)
    private val id = session.getUID()
    val enrollID = session.getEnrollmentID()

    fun eventDetails(eid:Int, callback: (EventsSchema?, Boolean?) -> Unit){
        val call = RetrofitInstances.service
        call.getEventDetails(enrollID,eid).enqueue(object: Callback<EventsSchema>{
            override fun onResponse(
                call: Call<EventsSchema>,
                response: Response<EventsSchema>
            ) {
                Log.w("check-kar", response.toString())
                response.body()?.let {
                    callback(it,false)
                }
            }
            override fun onFailure(call: Call<EventsSchema>, t: Throwable) {
                callback(null,true)
            }
        })
    }
    fun getAdminEventbyId(eid:Int){
        eventResponseLiveData.postValue(
            Resource.Loading()
        )
        Log.w("sebastian evm",eid.toString())
            RetrofitInstances.service.getEventbyId(eid)
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        Log.w("sebastian evm", response.body().toString())

                        if(response.isSuccessful){
                            response.body()?.let{
                                eventResponseLiveData.postValue(
                                    Resource.Success(
                                        it
                                    )
                                )
                            }
                        }
                        Log.w("Sebastian", eventResponseLiveData.value.toString())
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

    fun registerUserForEvent(eid:Int){
            RetrofitInstances.service.registerUserForRegistration(id, eid)
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
    fun registerTeamForEvent(team:TeamSchema, callback:(Boolean?,String?)->Unit){
        RetrofitInstances.service.registerTeamforEvents(team)
            .enqueue(object: Callback<StatusModel>{

                override fun onResponse(call: Call<StatusModel>, response: Response<StatusModel>) {
                    response.body()?.let {
                        callback(it.success,
                        it.message)
                    }
                }

                override fun onFailure(call: Call<StatusModel>, t: Throwable) {
                    callback(false,"Something went Wrong! Try Again")
                }

            })
    }
}