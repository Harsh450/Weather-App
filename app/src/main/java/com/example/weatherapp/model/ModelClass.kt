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

var defaultCity = ModelClass(
    weather = listOf<Weather>(
        Weather(
            id = 711, main = "Smoke", description = "smoke", icon = "50n"
        )
    ),
    main = Main(
        temp = 295.17,
        feels_like = 294.47,
        temp_min = 295.17,
        temp_max = 295.17,
        pressure = 1017,
        humidity = 40
    ),
    wind = Wind(speed = 4.12, deg = 50),
    sys = Sys(type = 1, id = 9049, country = "IN", sunrise = 1672883485, sunset = 1672922252),
    id = 1279233,
    name = "Ahmedabad"
)

//{
//    "coord": {
//    "lon": 72.6167,
//    "lat": 23.0333
//},
//    "weather": [
//    {
//        "id": 711,
//        "main": "Smoke",
//        "description": "smoke",
//        "icon": "50n"
//    }
//    ],
//    "base": "stations",
//    "main": {
//    "temp": 295.17,
//    "feels_like": 294.47,
//    "temp_min": 295.17,
//    "temp_max": 295.17,
//    "pressure": 1017,
//    "humidity": 40
//},
//    "visibility": 4000,
//    "wind": {
//    "speed": 4.12,
//    "deg": 50
//},
//    "clouds": {
//    "all": 0
//},
//    "dt": 1672922365,
//    "sys": {
//    "type": 1,
//    "id": 9049,
//    "country": "IN",
//    "sunrise": 1672883485,
//    "sunset": 1672922252
//},
//    "timezone": 19800,
//    "id": 1279233,
//    "name": "Ahmedabad",
//    "cod": 200
//}
