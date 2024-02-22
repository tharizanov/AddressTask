package com.example.task.address.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val retrofit = Retrofit.Builder()
    .baseUrl(AddressRepository.API_URL)
    .client(OkHttpClient().newBuilder().build())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val repository = AddressRepository(retrofit.create(ApiService::class.java))