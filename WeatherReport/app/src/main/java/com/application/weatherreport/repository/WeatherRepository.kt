package com.application.weatherreport.repository


import com.example.room.mvvm.`interface`.RetrofitService


class WeatherRepository constructor(private val retrofitService: RetrofitService) {

    fun getWeatherDetail(city:String,units:String,appid:String) = retrofitService.getWeatherDetail(city,units,appid)



}
