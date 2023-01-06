package com.example.weatherapp.repository

import android.util.Log
import com.example.weatherapp.apiservice.ApiCall
import com.example.weatherapp.fragment.WeatherDashboard.Companion.API_KEY
import com.example.weatherapp.model.ModelClass

class WeatherRepository {

    suspend fun getCurrentWeatherData(latitude: String, longitude: String): ModelClass? {
        return ApiCall.getApiInterface()?.getCurrentWeatherData(latitude, longitude, API_KEY)
    }

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