package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class AllDataModelClass(
    @SerializedName("weather") val weatherData: List<WeatherData>? = null,
    @SerializedName("main") val mainData: MainData? = null,
    @SerializedName("wind") val windData: WindData? = null,
    @SerializedName("sys") val systemData: SystemData? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
)
