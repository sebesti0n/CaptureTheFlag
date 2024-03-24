package com.sebesti0n.capturetheflag.apiServices
import com.sebesti0n.capturetheflag.secrets.Secrets
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitInstances {
    companion object {

        private val BASEURL = "https://be87-49-38-250-109.ngrok-free.app/"
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
        val ctfServices: CtfApiEndpoints by lazy {
            retrofit.create(CtfApiEndpoints::class.java)
        }
    }
}

