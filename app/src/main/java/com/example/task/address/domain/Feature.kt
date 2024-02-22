package com.example.task.address.domain

data class Feature(
    val type: String?,
    val geometry: FeatureGeometry?,
    val properties: FeatureProperties?
)
