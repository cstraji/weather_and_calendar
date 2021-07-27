package com.application.weatherreport.common

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {


lateinit var utils: Utils
lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         utils=Utils.getwebservice(this)
        context=applicationContext

    }
    fun chkStatus(context:Context):Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifi!!.isConnectedOrConnecting) {
            // Toast.makeText(this, "Wifi", Toast.LENGTH_LOeNG).show()
            return true
        } else if (mobile!!.isConnectedOrConnecting) {
            //    Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show()
        } else {
            //  Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show()
        }
        return false

    }
    fun showInternetDialog(context: Context)
    {
        Toast.makeText(context,"Please check your Internet connection",Toast.LENGTH_LONG).show()
    }

}