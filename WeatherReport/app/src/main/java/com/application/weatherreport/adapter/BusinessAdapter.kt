package com.application.weatherreport.adapter

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.weatherreport.R
import com.application.weatherreport.common.Utils
import com.application.weatherreport.databinding.ItemdialogBinding
import com.application.weatherreport.model.BusinessModel
import com.application.weatherreport.model.DayModel
import com.application.weatherreport.model.Hours
import com.application.weatherreport.viewmodel.BusinessViewModel
import java.util.*
import kotlin.collections.ArrayList


class BusinessAdapter(val viewModel: BusinessViewModel, val arrayList: MutableList<BusinessModel>, val context: Context): RecyclerView.Adapter<BusinessAdapter.ItemViewHolder>() {
    var utils=Utils.getwebservice(context)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BusinessAdapter.ItemViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val binding=ItemdialogBinding. inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BusinessAdapter.ItemViewHolder, position: Int) {
        holder.bind(arrayList.get(position),position)
    }

    override fun getItemCount(): Int {
        if(arrayList.size==0){
           // Toast.makeText(context,"List is empty", Toast.LENGTH_LONG).show()
        }else{


        }
        return arrayList.size
    }


    inner  class ItemViewHolder(private val binding: ItemdialogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusinessModel,position: Int){
          //  var p=position

            if(position != arrayList.size-1)
            {
                binding.btnadd.visibility= View.GONE

            }
            var fromtime:String=""
            var totime:String=""
            if(item.hours!!.size>0 )  {
                binding.txtfromtime.text = utils.convertTime12Hour(item.hours!!.get(0).start_hours)
                binding.txttotime.text = utils.convertTime12Hour(item.hours!!.get(0).end_hours)
                fromtime=item.hours!!.get(0).start_hours
                totime=item.hours!!.get(0).end_hours

            }
            binding.btnadd.setOnClickListener {

                if(fromtime.equals(""))
                {
                    utils.showToast(context,"Select From Time")
                }
               else if(totime.equals(""))
                {
                    utils.showToast(context,"Select To Time")
                }else
                {
                    if(item.days!!.size==0)
                    {
                        utils.showToast(context,"Select Minimum One Day")
                    }else
                    {
                        if(item.days!!.contains("0")) {
                            utils.showToast(context,"All Days are selected")
                        }else{
                            item.hours!!.removeAll(item.hours!!)
                            val hours = Hours(totime, fromtime)
                            item.hours!!.add(hours)
                            viewModel.add()
                        }

                    }
                }
            }
            binding.btndelete.setOnClickListener {
                if(arrayList.size>1) {
                    viewModel.remove(item)

                }
            }
            binding.txtfromtime.setOnClickListener {
                val mcurrentTime: Calendar = Calendar.getInstance()
                val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute: Int = mcurrentTime.get(Calendar.MINUTE)
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context,
                    { timePicker, selectedHour, selectedMinute ->

                         fromtime = "$selectedHour:$selectedMinute"

                        binding.txtfromtime.setText(Utils.getwebservice(context).convertTime12Hour(fromtime))

                    },
                    hour,
                    minute,
                    false
                ) //Yes 24 hour time

                binding.txttotime.text="To Time"
                mTimePicker.show()
            }
            binding.txttotime.setOnClickListener {
                if(!fromtime.equals("")) {
                    val mcurrentTime: Calendar = Calendar.getInstance()
                    val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                    val minute: Int = mcurrentTime.get(Calendar.MINUTE)
                    val mTimePicker: TimePickerDialog
                    mTimePicker = TimePickerDialog(
                        context,
                        { timePicker, selectedHour, selectedMinute ->

                            totime = "$selectedHour:$selectedMinute"
                            //convert date 12 hour format
                            binding.txttotime.setText(
                                Utils.getwebservice(context).convertTime12Hour(totime)
                            )
                            //check date 24 hrs format
                            var status=Utils.getwebservice(context).checkDate(fromtime, totime)

                            if(!status) {
                                totime = ""
                                binding.txttotime.setText("To Time"
                                )
                            }else
                            {
                                item.hours!!.removeAll(item.hours!!)
                                val hours= Hours(totime,fromtime)
                                item.hours!!.add(hours)
                                viewModel.updateHours(arrayList)

                            }

                        },
                        hour,
                        minute,
                        false
                    ) //Yes 24 hour time

                    mTimePicker.show()
                }else
                {
                    Toast.makeText(context,"Select From Time",Toast.LENGTH_SHORT).show()
                }
            }
            binding.daysrecycler.layoutManager=GridLayoutManager(context,4)
            binding.daysrecycler.adapter=DaysAdapter(item.dayslist!!,context,item.days!!,object :DaysAdapter.BtnClickListener   {
                override fun onBtnClick(position: Int, data: DayModel) {
                    Log.e("TAG",data.dayname+data.checked+data.index)
                    if(data.checked) {
                        if(data.index.equals("0"))
                        {

                            viewModel.selectAllDays(item)
                        }else {
                            if(item.days!!.contains("0")) {
                                item.days!!.remove("0")
                                item.dayslist!!.get(0).checked = false

                            }
                            item.days!!.add(data.index)
                            item.dayslist!!.get(position).checked = true
                        }
                    }
                    else {
                        item.days!!.remove(data.index)
                        item.dayslist!!.get(position).checked=false

                    }

                    notifyDataSetChanged()

                    //Log.e("TAG",item.days.toString())
             }

            })

        }

    }
fun showAlert(context: Context)
{
    val builder = AlertDialog.Builder(context)
    //set title for alert dialog
    builder.setTitle(R.string.dialogTitle)
    //set message for alert dialog
    builder.setMessage(R.string.dialogMessage)
    builder.setIcon(android.R.drawable.ic_dialog_alert)

    //performing positive action
    builder.setPositiveButton("Ok"){dialogInterface, which ->
        Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
    }
    //performing cancel action
    builder.setNeutralButton("Cancel"){dialogInterface , which ->
        Toast.makeText(context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
    }
    /*//performing negative action
    builder.setNegativeButton("No"){dialogInterface, which ->
        Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
    }
    */// Create the AlertDialog
    val alertDialog: AlertDialog = builder.create()
    // Set other dialog properties
    alertDialog.setCancelable(false)
    alertDialog.show()
}
}