package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class ModelClass(
    @SerializedName("weather") val weather: List<Weather>? = null,
    @SerializedName("main") val main: Main? = null,
    @SerializedName("wind") val wind: Wind? = null,
    @SerializedName("sys") val sys: Sys? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null

)
