package com.application.weatherreport.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.ParseException
import android.os.Build
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Utils(var context:Context) {
    var firsttimelanch:Boolean
        get() {

            val sharedPref = context.getSharedPreferences(FIRSTLANCH, Context.MODE_PRIVATE)
            return sharedPref.getBoolean(FIRSTLANCH, false)
        }
        set(firsttimelanch) {
            val sharedPref = context.getSharedPreferences(FIRSTLANCH, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean(FIRSTLANCH, firsttimelanch)
            editor.commit()
        }

    var cityName:String?
        get() {

            val sharedPref = context.getSharedPreferences(CITY, Context.MODE_PRIVATE)
            return sharedPref.getString(CITY, "")
        }
        set(cityName) {
            val sharedPref = context.getSharedPreferences(CITY, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(CITY, cityName)
            editor.commit()
        }
    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
    fun convertTimeFormat(selectedHour: Int,minute:Int):String{
        var am_pm: String=""

        val datetime = Calendar.getInstance()
        datetime[Calendar.HOUR_OF_DAY] = selectedHour
        datetime[Calendar.MINUTE] = minute

        if (datetime[Calendar.AM_PM] === Calendar.AM)
            am_pm =    "AM"
        else if (datetime[Calendar.AM_PM] === Calendar.PM)
            am_pm = "PM"

        val strHrsToShow =
            if (datetime[Calendar.HOUR] === 0) "12"
            else
                datetime[Calendar.HOUR].toString() + ""

        return strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm

    }
    fun convertTime12Hour(time:String):String{
        val fmt = SimpleDateFormat("HH:mm")
        var date: Date? = null
        try {
            date = fmt.parse(time)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }

        val fmtOut = SimpleDateFormat("hh:mm aa")

        val formattedTime: String = fmtOut.format(date)

        return formattedTime

    }


    fun checkDate(mEntryTime:String,mExitTime: String):Boolean{
        var status:Boolean=false
        try {
            val sdf = SimpleDateFormat("hh:mm")
            val inTime: Date = sdf.parse(mEntryTime)
            val outTime: Date = sdf.parse(mExitTime)


           if (isTimeAfter(inTime, outTime)) {
               // Toast.makeText(context, "Time validation success", Toast.LENGTH_LONG).show();
                var difference: Long = outTime.getTime() - inTime.getTime()
                if (difference < 0) {
                    val dateMax: Date = sdf.parse("24:00")
                    val dateMin: Date = sdf.parse("00:00")
                    difference =
                        dateMax.time - inTime.getTime() + (outTime.getTime() - dateMin.time)
                    //Log.e("TAG","Diffferee===="+difference)
                }
                val days = (difference / (1000 * 60 * 60 * 24)).toInt()
                val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
                val min =
                    (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
              //  Log.e("log_tag", "Hours: $hours, Mins: $min")
                if(hours>=1)
                {
                   // Log.e("log_tag", "Hours greater 1: $hours, Mins: $min")
                    status=true
                }else

                {
                   // Log.e("log_tag", "hours less 1=====: $hours, Mins: $min")
                       showToast(context,"Select Minimum 1 hours")
                    status= false

                }
            }
            else {

                showToast(context,"Select To Time within 24 hours format")
                              status= false
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            //Toast.makeText(AddActivity.this, "Parse error", Toast.LENGTH_LONG).show();
        }
        return status
    }
    fun  isTimeAfter(startTime: Date, endTime:Date):Boolean {
        return !endTime.before(startTime)
    }

    fun noInternetDialog()
    {

            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show()

    }
    fun showToast(context: Context,message:String)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    val FIRSTLANCH = "firstlanch"
    val CITY = "city"


    companion object {

        private var sInstance: Utils? = null
        // Getter to access Singleton instance
        fun getwebservice(context: Context): Utils {

            if (sInstance == null) {
                synchronized(Utils::class) {
                    if (sInstance == null) {
                        sInstance = Utils(context)
                    }
                }
            } else {
            }
            return sInstance!!
        }
    }
}