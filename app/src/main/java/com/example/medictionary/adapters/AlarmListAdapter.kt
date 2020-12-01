package com.example.medictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medictionary.R
import com.example.medictionary.models.AlarmModel
import kotlinx.android.synthetic.main.alarmrow.view.*

class AlarmListAdapter(private val alarms:MutableList<AlarmModel>):RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val alarm_nameTxt:TextView=itemView.alarm_nameTxt
        val timeTxt:TextView=itemView.timeTxt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.alarmrow,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.alarm_nameTxt.text=alarms[position].name
        holder.timeTxt.text=alarms[position].time
    }

    override fun getItemCount()=alarms.size


}