package com.application.weatherreport.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.application.weatherreport.R
import com.application.weatherreport.common.BaseActivity
import com.google.android.gms.location.*
import java.util.*


class MainActivity : BaseActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitude:Double=0.0
    var longitude:Double=0.0
    private var PERMISSION_ID = 44
    var geocoder: Geocoder? = null
    var city:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * get current location lat ,long
         */
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                       // latitudeTextView.setText(location.latitude.toString() + "")
                       // longitTextView.setText(location.longitude.toString() + "")
                        Log.e("MAii",location.latitude.toString())
                        Log.e("MAii",location.longitude.toString())
                        latitude=location.latitude
                        longitude=location.longitude
                        getAddress()



                    }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            //Log.e("lastMAii",mLastLocation.latitude.toString())
           // Log.e("lastMAii",mLastLocation.longitude.toString())
            latitude=mLastLocation.latitude
            longitude=mLastLocation.longitude

           getAddress()

        }
    }
    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    // method to request for permissions
    private fun requestPermissions() {
        requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (utils.isInternetAvailable(this))
                    getLastLocation()
                else
                    showInternetDialog(this)

            }
        }
    }
    /**
     * getAddress() used for convert lat,long to address for take city"
     */
    fun getAddress(){
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder!!.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        city  = addresses[0].getLocality()
        if(city!=null)
        {
            utils.cityName=city

            val s=Intent(this@MainActivity,WeatherActivity::class.java)
            s.putExtra("latitude",latitude)
            s.putExtra("longitude",longitude)
            s.putExtra("city",city!!)
            startActivity(s)
            finish()
        }
        /* val state: String = addresses[0].getAdminArea()
         val country: String = addresses[0].getCountryName()
         val postalCode: String = addresses[0].getPostalCode()
         val knownName: String = addresses[0].getFeatureName()*/
    }
   fun initView()
    {
        if(utils.isInternetAvailable(this))
            getLastLocation()
        else {

            showInternetDialog(this)
            if( utils.cityName!=null&&utils.firsttimelanch==true)
            {
                val s=Intent(this@MainActivity,WeatherActivity::class.java)

                s.putExtra("city",utils.cityName)
                startActivity(s)
                finish()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        initView()    }




}
