package com.example.weatherapp.apiservice

import com.example.weatherapp.model.ModelClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    suspend fun getCityWeatherData(
        @Query("q") cityName: String,
        @Query("APPID") api_key: String,
    ) : ModelClass
}