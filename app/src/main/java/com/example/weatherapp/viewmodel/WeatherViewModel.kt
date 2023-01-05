package com.example.weatherapp.viewmodel

//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.weatherapp.model.ModelClass
//import com.example.weatherapp.repository.WeatherRepository
//import kotlinx.coroutines.launch
//
//class WeatherViewModel : ViewModel() {
//
//    var data: MutableLiveData<ModelClass?> = MutableLiveData(ModelClass())
//    var repository: WeatherRepository = WeatherRepository()
//
//    fun getCurrentWeatherData(latitude: String, longitude: String) {
//        viewModelScope.launch {
//           val weatherData = repository.getCurrentWeatherData(latitude, longitude)
//            data.value = weatherData
//        }
//    }
//    fun getCityWeatherData(cityName: String, api_key: String) {
//        viewModelScope.launch {
//           val cityWeatherData = repository.getCityWeatherData(cityName, api_key)
//            data.value = cityWeatherData
//        }
//    }
//}