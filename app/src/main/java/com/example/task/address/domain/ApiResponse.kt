package com.example.task.address.domain

data class ApiResponse(
    val type: String?,
    val version: String?,
    val features: List<Feature>?,
    val attribution: String?,
    val license: String?,
    val query: String?,
    val limit: Int?
)
