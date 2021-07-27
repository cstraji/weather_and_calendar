package com.application.employee.viewmodel

import androidx.lifecycle.*import com.application.weatherreport.model.weatherModel
import com.application.weatherreport.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response


class WeatherViewModel constructor(private  val weatherRepository: WeatherRepository):ViewModel() {
    val weatherModel=MutableLiveData<weatherModel>()
    val errorMessage=MutableLiveData<String>()
    val myLiveData = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()



    fun getWeatherDetail(city:String, appid:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val respose=weatherRepository.getWeatherDetail(city,"metric",appid)
            withContext(Dispatchers.Main) {
                respose.enqueue(object : retrofit2.Callback<weatherModel> {
                    override fun onResponse(
                        call: Call<weatherModel>,
                        response: Response<weatherModel>
                    ) {
                        weatherModel.postValue(response.body())
                        loading.value=false
                    }

                    override fun onFailure(call: Call<weatherModel>, t: Throwable) {
                        errorMessage.postValue(t.message)
                        loading.value=false

                    }

                })
            }
        }



    }



}