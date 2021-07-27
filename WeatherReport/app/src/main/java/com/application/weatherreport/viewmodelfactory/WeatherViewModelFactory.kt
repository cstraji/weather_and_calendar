package com.application.employee.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.employee.viewmodel.WeatherViewModel
import com.application.weatherreport.repository.WeatherRepository

class WeatherViewModelFactory constructor(private val repository: WeatherRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            WeatherViewModel(this.repository) as T

        }else
        {
            throw IllegalArgumentException("ViewModel Not Found")
        }
   }
}