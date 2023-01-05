package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.ModelClass
import com.example.weatherapp.model.defaultCity
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var data: MutableLiveData<ModelClass?> = MutableLiveData(defaultCity)
    var repository: WeatherRepository = WeatherRepository()

    fun getCityWeatherData(cityName: String, api_key: String) {
        viewModelScope.launch {
           val cityWeatherData = repository.getCityWeatherData(cityName, api_key)
            data.value = cityWeatherData
        }
    }
}