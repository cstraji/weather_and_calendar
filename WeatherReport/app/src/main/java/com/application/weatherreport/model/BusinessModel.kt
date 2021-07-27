package com.application.weatherreport.model

import com.google.gson.annotations.SerializedName

data class BusinessModel (

    @SerializedName("days") var days : ArrayList<String>?,
    @SerializedName("hours") var hours : ArrayList<Hours>?,
    @SerializedName("dayslist") var dayslist:MutableList<DayModel>?
)
data class Hours (

    @SerializedName("end_hours") var end_hours : String,
    @SerializedName("start_hours") var start_hours : String
)
data class DayModel(
    var dayname:String,var checked:Boolean,var index:String
)
data class SaveModel(
    @SerializedName("days") var days : ArrayList<String>?,
    @SerializedName("hours") var hours : ArrayList<Hours>?,
)