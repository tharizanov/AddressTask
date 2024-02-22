package com.example.task.address.network

import com.example.task.address.domain.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/")
    suspend fun searchAddresses(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int
    ): ApiResponse
}