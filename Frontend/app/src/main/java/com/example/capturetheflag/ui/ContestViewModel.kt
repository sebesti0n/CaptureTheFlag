package com.example.capturetheflag.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.NextRiddleModel
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.models.ResponseQuestionModel
import com.example.capturetheflag.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestViewModel(
    app:Application
) : AndroidViewModel(app) {

    private var riddlesLivedata= MutableLiveData<ArrayList<QuestionModel>>()
    fun get():LiveData<ArrayList<QuestionModel>>{
        return riddlesLivedata
    }
    private val session = Session.getInstance(app.applicationContext)
    fun getUID() = session.getUID()
    private val id = session.getUID()
    fun getRiddles(eid: Int) {
            try {
                val response = RetrofitInstances.service.getRiddles(eid, id)
                response.enqueue(object : Callback<ResponseQuestionModel>{
                    override fun onResponse(
                        call: Call<ResponseQuestionModel>,
                        response: Response<ResponseQuestionModel>
                    ) {
                        val rList = response.body()!!.riddles
                        Log.i("seb contest VM","response ${response} \n qList ${rList}")
                        riddlesLivedata.postValue(rList)
                    }

                    override fun onFailure(call: Call<ResponseQuestionModel>, t: Throwable) {
                        Log.d("sebastian riddleList", "Unsuccessful response or empty body")
                    }

                })
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("sebastian riddleList", "Exception occurred: ${e.message}")
            }

    }
    fun getSubmissionDetails(eid: Int,callback:(Int?)->Unit){
            try {
                val res = RetrofitInstances.service.getSubmissionDetails(eid, id)
                res.enqueue(object: Callback<NextRiddleModel>{
                    override fun onResponse(
                        call: Call<NextRiddleModel>,
                        response: Response<NextRiddleModel>
                    ) { Log.d("sebastian submissionDetails",response.body().toString())
                        val rNo = response.body()
                        rNo?.let {
                            callback(it.next)
                        }
                    }

                    override fun onFailure(call: Call<NextRiddleModel>, t: Throwable) {
                        Log.d("sebastian riddleNo", "getting error while this")
                    }

                })
            }catch(e:Exception){
                e.printStackTrace()
                Log.d("sebastian riddleno", "Exception occurred: ${e.message}")
            }
        }

    fun getRiddleNumberNumberFirst(eid:Int, callback:(Int?)->Unit){
        try {
            val res = RetrofitInstances.service.getSubmissionDetails(eid, id)
            res.enqueue(object: Callback<NextRiddleModel>{
                override fun onResponse(
                    call: Call<NextRiddleModel>,
                    response: Response<NextRiddleModel>
                ) { Log.d("sebastian submissionDetails",response.body().toString())
                    val rNo = response.body()
                    rNo?.let {
                        callback(it.next);
                    }
                }

                override fun onFailure(call: Call<NextRiddleModel>, t: Throwable) {
                    Log.d("sebastian riddleNo First1", "getting error while this")
                }

            })
        }catch(e:Exception){
            e.printStackTrace()
            Log.d("sebastian riddleno First2", "Exception occurred: ${e.message}")
        }
    }
}