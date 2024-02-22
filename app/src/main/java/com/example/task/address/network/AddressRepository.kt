package com.example.task.address.network

import com.example.task.address.domain.ApiResponse

class AddressRepository(private val apiService: ApiService) {

    companion object {
        const val API_URL = "https://api-adresse.data.gouv.fr"
    }

    suspend fun searchAddresses(
        query: String,
        type: String = "",
        limit: Int = 5
    ): ApiResponse = apiService.searchAddresses(query, type, limit)

}