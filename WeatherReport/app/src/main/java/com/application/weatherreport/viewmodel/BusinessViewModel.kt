package com.application.weatherreport.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.weatherreport.model.BusinessModel
import com.application.weatherreport.model.DayModel
import com.application.weatherreport.model.SaveModel

class BusinessViewModel:ViewModel() {
    var businesslist= MutableLiveData<MutableList<BusinessModel>>()
    var newlist = arrayListOf<BusinessModel>()
    var weekdays= arrayListOf<String>("Alldays","Sunday","Monday","Thuesday","Wednesday","Thursday","Friday","Saturday")
    var dayslist:MutableList<DayModel> = mutableListOf()
    var selectdayname= MutableLiveData<String>()

    fun addWeekDays():MutableList<DayModel>
    {
      //  var dayslist:MutableList<DayModel> = mutableListOf()

        for(i in 0..weekdays.size-1)
            dayslist.add(DayModel(weekdays.get(i),false,i.toString(),))
        return dayslist
    }
    fun add(){
      // var data=BusinessModel(arrayListOf(), arrayListOf(),addWeekDays())
        var data=BusinessModel(arrayListOf(), arrayListOf(),dayslist )

        newlist.add(data)
        businesslist.postValue(newlist)
    }
    fun updateHours(templist:MutableList<BusinessModel>)
    {
        businesslist.postValue(templist)
    }
    fun save():Int
    {
        var status=0
        for(data:BusinessModel in newlist)
        {
            if(data.days!!.size==0)
            {
              status= 1
                break
            }else if(data.hours!!.size==0)
            {
                status= 2
                break
            }else if(data.hours!!.get(0).start_hours.equals(""))
            {
                status= 3
                break
            }
            else if(data.hours!!.get(0).end_hours.equals(""))
            {
                status= 4
                break
            }else {
                status = 0
            }


        }
        return status
    }
    fun showSelectDays()
    {
        var daylist:ArrayList<String> = arrayListOf()
        for(data:BusinessModel in newlist)
        {
            for(e:String in data.days!!)
             daylist.add(e)

        }
        val s = daylist.toSet().toList();
        var name:String=""
        for(e:String in s)
        {
            if(e.equals("0"))
            {
                for(i in 1..weekdays.size-1)
                name=name+" "+weekdays[i]

            }else
             name=name+" "+weekdays[e.toInt()]
          //  Log.e("TAG","Name=="+name)
        }
        selectdayname.value=name
        showJSON()

    }
    fun showJSON()
    {
        var templist:MutableList<SaveModel> = arrayListOf()

        for(data:BusinessModel in newlist)
            templist.add(SaveModel(data.days!!,data.hours!!))

         Log.e("TAG","FinalJSON====="+templist.toString())
    }
        fun selectAllDays(item:BusinessModel)
        {
            for(i in 0 ..dayslist!!.size-1){
                if(i==0)
                    dayslist!!.get(i).checked=true
                else
                    dayslist!!.get(i).checked=false
            }
            for(data:BusinessModel in newlist)
            {
                data.days!!.removeAll(data.days!!)
            }
            item.days!!.add("0")

            businesslist.postValue(newlist)
            clearAllItem(item)
           // item.days!!.removeAll(item.days!!)
        }
    fun clearAllItem(item: BusinessModel)
    {
        newlist.removeAll(newlist)
        newlist.add(item)
        businesslist.postValue(newlist)
    }
    fun remove(item: BusinessModel){
        newlist.remove(item)
        for(i in 0..item.dayslist!!.size-1) {
            for(j in 0..item.days!!.size-1) {
                if (item.dayslist!!.get(i).index.equals(item.days!!.get(j)))
                    item.dayslist!!.get(i).checked = false
            }
        }
        businesslist.postValue(newlist)
    }

}