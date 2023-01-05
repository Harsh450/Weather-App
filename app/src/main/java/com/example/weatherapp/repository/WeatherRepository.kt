package com.example.weatherapp.repository
//
//import com.example.weatherapp.apiservice.ApiCall
//import com.example.weatherapp.fragment.WeatherDashboard.Companion.API_KEY
//import com.example.weatherapp.model.ModelClass
//import retrofit2.http.Query
//
//class WeatherRepository {
//
//    suspend fun getCurrentWeatherData(latitude: String, longitude: String): ModelClass? {
//        return ApiCall.getApiInterface()?.getCurrentWeatherData(latitude, longitude, API_KEY)
//    }
//
//    suspend fun getCityWeatherData(
//        cityName: String,
//        api_key: String,
//    ): ModelClass? {
//        return ApiCall.getApiInterface()?.getCityWeatherData(cityName,api_key)
//    }
//}