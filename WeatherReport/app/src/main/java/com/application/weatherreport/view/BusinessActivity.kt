package com.application.weatherreport.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.employee.viewmodel.WeatherViewModel
import com.application.employee.viewmodelfactory.WeatherViewModelFactory
import com.application.weatherreport.R
import com.application.weatherreport.adapter.BusinessAdapter
import com.application.weatherreport.common.BaseActivity
import com.application.weatherreport.databinding.ActivityBusinessBinding
import com.application.weatherreport.databinding.BottomdialogBinding
import com.application.weatherreport.model.BusinessModel
import com.application.weatherreport.model.DayModel
import com.application.weatherreport.model.Hours
import com.application.weatherreport.repository.WeatherRepository
import com.application.weatherreport.viewmodel.BusinessViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class BusinessActivity : BaseActivity() {
    lateinit var binding: ActivityBusinessBinding
    lateinit var viewModel:BusinessViewModel
    var days:ArrayList<String>?= arrayListOf()
    var TAG:String=BusinessActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
        binding= ActivityBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel= ViewModelProvider(this).get(BusinessViewModel::class.java)
        //Show bottom dialog days
        binding.showbutton.setOnClickListener {
            showBottomSheetDialog()
        }
        //first time add days for recycler view
        viewModel.addWeekDays()
        //initialize empty view for add new item
        viewModel.add()

        //show selected week days name when save button click
        viewModel.selectdayname.observe(this, Observer {
            if(it!=null)
            {
                binding.txtalldays.text=it
            }
        })

  }

    //initialize adapter for recycler view
    private fun initialiseAdapter(binding: BottomdialogBinding){

        binding.bottomRecyerview.layoutManager=LinearLayoutManager(this)
        viewModel.businesslist.observe(this, Observer{
            Log.i("data",it.toString())
            binding.bottomRecyerview.adapter= BusinessAdapter(viewModel, it, this)
        })
    }




    private fun showBottomSheetDialog() {
       val bottomSheetDialog = BottomSheetDialog(this)
        val inflater = LayoutInflater.from(this)

        var binding= BottomdialogBinding.inflate(inflater)
      bottomSheetDialog. setContentView(binding.root)
        initialiseAdapter(binding)
        binding.button.setOnClickListener {
            //call save function for validation
          var status=  viewModel.save()
            when(status)
            {
                1-> utils.showToast(context,"Select Aleast one Day")
                2-> utils.showToast(context,"Select From and To Time")
                3-> utils.showToast(context,"Select From Time")
                4-> utils.showToast(context,"Select To Time")
                0->{
                    //utils.showToast(context,"sucess")
                    bottomSheetDialog.dismiss()
                    //show all seleted day name in textview
                    viewModel.showSelectDays()
                }




            }

        }

        if(bottomSheetDialog.isShowing)
            bottomSheetDialog.dismiss()
        bottomSheetDialog.show()

    }
}