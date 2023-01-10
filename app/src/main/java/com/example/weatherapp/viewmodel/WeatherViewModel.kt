package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AllDataModelClass
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var data: MutableLiveData<AllDataModelClass?> = MutableLiveData(AllDataModelClass())
    var repository: WeatherRepository = WeatherRepository()

    fun getCurrentWeatherData(latitude: String, longitude: String) {
        viewModelScope.launch {
            val weatherData = repository.getCurrentWeatherData(latitude, longitude)
            Log.d("ABCD","${weatherData.toString()}")
            data.value = weatherData
        }
    }

    fun getCityWeatherData(cityName: String, api_key: String) {
        viewModelScope.launch {
           val cityWeatherData = repository.getCityWeatherData(cityName, api_key)
            data.value = cityWeatherData
        }
    }
}