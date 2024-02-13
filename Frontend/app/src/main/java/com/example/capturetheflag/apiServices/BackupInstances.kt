package com.example.capturetheflag.apiServices

import com.example.capturetheflag.models.StatusModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class BackupInstances {
    companion object {

        private val BASEURL="https://6469-106-67-32-63.ngrok-free.app/"
//            "https://ctf-ndp3.onrender.com/"

        private val retrofit: Retrofit by lazy {
            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build()
            Retrofit.Builder().baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        val service: BackupEndpoints by lazy {
            retrofit.create(BackupEndpoints::class.java)
        }
    }
    interface BackupEndpoints {
        @GET("/all_is_well")
        fun allOkay(): Call<StatusModel>
    }
}


