package com.example.openai.AI.Weather

data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherDetail>
)

data class Main(
    val temp: Double // Temperature in Celsius (if units="metric" is used)
)

data class WeatherDetail(
    val main: String, // Example: "Rain", "Clear", "Cloudy"
    val description: String // Example: "light rain", "scattered clouds"
)