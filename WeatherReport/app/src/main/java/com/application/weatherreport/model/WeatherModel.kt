package com.application.weatherreport.model


data class weatherModel (

    val coord : Coord,
    val weather : List<Weather>,
    val base : String,
    val main : Main,
    val visibility : Int,
    val wind : Wind,
    val clouds : Clouds,
    val dt : Int,
    val sys : Sys,
    val timezone : Int,
    val id : Int,
    val name : String,
    val cod : Int
)
data class Clouds (

    val all : Int
)

data class Main (

    val temp : Double,
    val feels_like : Double,
    val temp_min : Double,
    val temp_max : Double,
    val pressure : Int,
    val humidity : Int
)
data class Sys (

    val type : Int,
    val id : Int,
    val country : String,
    val sunrise : Int,
    val sunset : Int
)
data class Weather (

    val id : Int,
    val main : String,
    val description : String,
    val icon : String
)
data class Wind (

    val speed : Double,
    val deg : Int
)
data class Coord (

    val lon : Double,
    val lat : Double
)