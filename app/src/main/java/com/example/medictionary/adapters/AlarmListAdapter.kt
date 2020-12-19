package com.example.medictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medictionary.R
import com.example.medictionary.interfaces.CellClickListener
import com.example.medictionary.models.AlarmModel
import kotlinx.android.synthetic.main.alarmrow.view.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmListAdapter(
    private val cellClickListener: CellClickListener,
    private val alarms: MutableList<AlarmModel>,
    private val format: String
):RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {
    val calendar = Calendar.getInstance()
    var _12HourSDF: SimpleDateFormat = SimpleDateFormat("hh:mm a")
    var _24HourSDF = SimpleDateFormat("HH:mm")
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val alarm_nameTxt:TextView=itemView.alarm_nameTxt
        val timeTxt:TextView=itemView.timeTxt
        val statueSwitch = itemView.statueSwitch
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.alarmrow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.alarm_nameTxt.text=alarms[position].name
        if(format == "12"){
            val _24HourDt = _24HourSDF.parse(alarms[position].time)
            holder.timeTxt.text=_12HourSDF.format(_24HourDt).toString()
        }else {
            holder.timeTxt.text=alarms[position].time
        }

        holder.statueSwitch.isChecked = (alarms[position].status == 1) && (alarms[position].lastDayOfTakingPill.toLong() > calendar.time.time)
        holder.itemView.statueSwitch.setOnCheckedChangeListener { _, _ -> cellClickListener.onCellClickListener(
            holder.itemView,alarms[position].id
        ) }
        holder.itemView.deleteButton.setOnClickListener {
            cellClickListener.onCellDeleteListener(holder.itemView,alarms[position].id, holder.adapterPosition)
        }
    }

    override fun getItemCount()=alarms.size


}