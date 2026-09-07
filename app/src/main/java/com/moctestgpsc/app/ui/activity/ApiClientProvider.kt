package com.moctestgpsc.app.ui.activity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.moctestgpsc.app.data.remote.ApiService

object ApiClientProvider {
    private const val BASE_URL = "https://api.moctestgpsc.com/" // Change to your API URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
