package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WindData(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int,

    )
