package com.example.airquality.retrofit

data class AirQualityResponse(
    val `data`: Data,
    val status: String // success
) {
    data class Data(
        val city: String, // Otlukbeli
        val country: String, // Turkey
        val current: Current,
        val location: Location,
        val state: String // Erzincan
    ) {
        data class Current(
            val pollution: Pollution,
            val weather: Weather
        ) {
            data class Pollution(
                val aqicn: Int, // 21
                val aqius: Int, // 56
                val maincn: String, // p2
                val mainus: String, // p2
                val ts: String // 2024-04-13T15:00:00.000Z
            )

            data class Weather(
                val hu: Int, // 74
                val ic: String, // 03n
                val pr: Int, // 1015
                val tp: Int, // 9
                val ts: String, // 2024-04-13T17:00:00.000Z
                val wd: Int, // 346
                val ws: Double // 3.14
            )
        }

        data class Location(
            val coordinates: List<Double>,
            val type: String // Point
        )
    }
}