package com.example.capturetheflag.apiServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstances {
    companion object {

        private val retrofit: Retrofit by lazy {
            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            Retrofit.Builder().baseUrl("https://e3eb-2409-40e5-104b-28b2-23c9-747e-368-384f.ngrok-free.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        val service: ApiEndpoints by lazy {
            retrofit.create(ApiEndpoints::class.java)
        }
    }
}

