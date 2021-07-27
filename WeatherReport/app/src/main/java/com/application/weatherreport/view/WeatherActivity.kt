package com.application.weatherreport.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.employee.viewmodel.WeatherViewModel
import com.application.employee.viewmodelfactory.WeatherViewModelFactory
import com.application.weatherreport.R
import com.application.weatherreport.common.BaseActivity
import com.application.weatherreport.databinding.ActivityWeatherBinding
import com.application.weatherreport.repository.WeatherRepository
import com.example.room.mvvm.`interface`.RetrofitService
import java.text.SimpleDateFormat
import java.util.*


class WeatherActivity : BaseActivity() {
    lateinit var binding: ActivityWeatherBinding
    var city:String?=null
    lateinit var viewModel: WeatherViewModel
    private val retrofitService= RetrofitService.getInstance()
    private val appid="8118ed6ee68db2debfaaa5a44c832918"
    private val TAG:String=WeatherActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        binding= ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        binding.btnnext.setOnClickListener {
            val intent=Intent(this,BusinessActivity::class.java)
            startActivity(intent)
        }

        /**
         * for hideing progressbar once api completed
         */
              viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun initView() {
        /**
         * create a object for viewmodel"
         */
        viewModel=ViewModelProvider(this,
            WeatherViewModelFactory(WeatherRepository(retrofitService))
        ).get(WeatherViewModel::class.java)
        /**
         * get value from mainAcitivity
         */
        val g=intent
        city=g.getStringExtra("city")

        val outFormat = SimpleDateFormat("EEEE")
        var daysName = outFormat.format(Date())

        daysName.let{
            binding.txtdate.text=it
        }
        city.let{
            binding.txtcity.text=it
        }
        /**
         * check condition for internet availability and call api
         */
        if(utils.isInternetAvailable(this))
            showWeatherDetail()
        else
            showInternetDialog(this)

    }

    fun showWeatherDetail() {
        /**
         * call weatherdetail from api
         */
        viewModel.getWeatherDetail(city!!, appid!!)
        viewModel.weatherModel.observe(this, Observer {
                if (it != null) {
                    it.main.temp.let {
                        binding.txttemp.text = it.toString() + "°C"
                    }
                    it.main.temp_min.let {
                        binding.txttempMin.text = "Min Temp: " + it.toString() + "°C"
                    }
                    it.main.temp_max.let {
                        binding.txttempMax.text = "Max Temp: " + it.toString() + "°C"
                    }
                    it.main.pressure.let {
                        binding.txtpressure.text = it.toString()
                    }
                    it.main.humidity.let {
                        binding.txthumidity.text = it.toString()
                    }
                    it.sys.sunrise.let {
                        binding.txtsunrise.text = SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(Date((it.toLong()) * 1000))
                    }
                    it.sys.sunset.let {
                        binding.txtsunset.text = SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(Date((it.toLong()) * 1000))
                    }
                    it.weather[0].description.let {
                        binding.txtdescription.text = it.capitalize()
                    }
                    it.wind.speed.let {
                        binding.txtwind.text = it.toString()
                    }
                    it.dt.let {
                        binding.txtupdateby.text = "Update By:" + SimpleDateFormat(
                            "dd/MM/yyyy hh:mm a",
                            Locale.ENGLISH
                        ).format(Date(it.toLong() * 1000))
                    }


                }

        })

    }
}