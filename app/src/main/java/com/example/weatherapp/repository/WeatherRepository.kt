package com.example.weatherapp.repository

import android.util.Log
import com.example.weatherapp.apiservice.ApiCall
import com.example.weatherapp.fragment.WeatherDashboard.Companion.API_KEY
import com.example.weatherapp.model.ModelClass

class WeatherRepository {

    suspend fun getCityWeatherData(
        cityName: String,
        api_key: String,
    ): ModelClass? {
        try {
            return ApiCall.getApiInterface()?.getCityWeatherData(cityName, api_key)
        } catch (e: Exception) {
            Log.d("City Not Found Error", "City not found : ${e.toString()}")
        }

        return null
    }
}