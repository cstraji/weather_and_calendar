package com.application.weatherreport.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.weatherreport.R
import com.application.weatherreport.databinding.DayitemBinding
import com.application.weatherreport.model.BusinessModel
import com.application.weatherreport.model.DayModel


class DaysAdapter(val arrayList: MutableList<DayModel>, val context: Context,val selectday :ArrayList<String>, val  btnlistener: BtnClickListener): RecyclerView.Adapter<DaysAdapter.ItemViewHolder>() {
    companion object {
        var mClickListener: BtnClickListener? = null
    }
    open interface BtnClickListener {
        fun onBtnClick(position: Int,item: DayModel)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DaysAdapter.ItemViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val binding=DayitemBinding. inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysAdapter.ItemViewHolder, position: Int) {
        holder.bind(arrayList.get(position),position,btnlistener)
    }

    override fun getItemCount(): Int {
        if(arrayList.size==0){

           // Toast.makeText(context,"List is empty", Toast.LENGTH_LONG).show()
        }else{


        }
        return arrayList.size
    }


    inner  class ItemViewHolder(private val binding: DayitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DayModel,position: Int,btnlistener:BtnClickListener){
            //disable checkbox for other list item if selected
            if(item.checked)
            {
                binding.checkBox.isEnabled=false
                //Log.e("TAG","Checkbox selected")
            }else {
                binding.checkBox.isEnabled = true
                binding.checkBox.isChecked = item.checked
            }

            binding.checkBox.text=item.dayname
            //highlight checked  current item selection
            for(i in 0..selectday.size-1)
            {
                if(item.index.equals(selectday[i]))
                {
                    binding.checkBox.isChecked=true
                    binding.checkBox.isEnabled=true

                }
            }


            binding.checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
              override  fun onCheckedChanged(
                    buttonView: CompoundButton?,
                    isChecked: Boolean
                ) {

                    if (isChecked) {
                        if(item.index.equals("0"))
                        {
                            item.checked=true
                            showAlert(context,item,position)


                        }else {
                            item.checked = true
                            if (btnlistener != null)
                                btnlistener?.onBtnClick(position,item)
                        }

                    } else {
                        item.checked=false
                        if (btnlistener != null)
                            btnlistener?.onBtnClick(position,item)

                    }


                }
            })

        }

    }
    fun showAlert(context: Context,item: DayModel,position: Int)
    {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Ok"){dialogInterface, which ->
            //  Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
            if (btnlistener != null)
                btnlistener?.onBtnClick(position,item)
        }
        //performing cancel action
       /* builder.setNeutralButton("Cancel"){dialogInterface , which ->
            //Toast.makeText(context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }
*/        /*//performing negative action
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