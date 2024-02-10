package com.example.capturetheflag.apiServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstances {
    companion object {
        private val BASEURL="https://ctf-ndp3.onrender.com/"
        private val retrofit: Retrofit by lazy {
            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            Retrofit.Builder().baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        val service: ApiEndpoints by lazy {
            retrofit.create(ApiEndpoints::class.java)
        }
    }
}

