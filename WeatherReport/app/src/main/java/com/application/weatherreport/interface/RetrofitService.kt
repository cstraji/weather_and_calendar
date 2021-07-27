package com.example.room.mvvm.`interface`

import com.application.weatherreport.model.weatherModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
   // @GET("movielist.json")
    @GET("data/2.5/weather")

 //   fun getWeatherDetail(): Call<List<WeatherModel>>
    fun getWeatherDetail(
        @Query("q") city:String,
        @Query("units") units: String,
        @Query("appid") appid: String
    ): Call<weatherModel>
    companion object {

        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}