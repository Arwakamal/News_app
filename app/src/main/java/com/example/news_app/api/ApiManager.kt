package com.route.newsc39.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiManager{
    companion object{
        const val apiKey = "a2803275cc264f5ab82151862011361a"

        private var retrofit: Retrofit? = null

        fun getWebServices(): WebServices{
            if(retrofit == null){
                val loggingInterceptor = HttpLoggingInterceptor {
                    Log.e("com.route.newsc39.api.ApiManager", "Body: $it") }
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit!!.create(WebServices:: class.java)
        }
    }
}