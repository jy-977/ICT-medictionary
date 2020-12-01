package com.example.medictionary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medictionary.*
import com.example.medictionary.adapters.AlarmListAdapter
import com.example.medictionary.extra.DBHandler
import com.example.medictionary.models.AlarmModel
import kotlinx.android.synthetic.main.fragment_pill_box.*


class PillBoxFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            val root = inflater.inflate(R.layout.fragment_pill_box, container, false)



        return inflater.inflate(R.layout.fragment_pill_box, container, false)
    }


    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        alarmrecylerView.apply {
            var list = mutableListOf<AlarmModel>()
            var idsList = mutableListOf<String>()
            var dbHelper = DBHandler(activity!!)

            val alarms = dbHelper.getAlarms
            if (alarms.count == 0) {
                showAlert("No results")
            } else {
                while (alarms.moveToNext()) {
                    list.add(AlarmModel(alarms.getString(2), alarms.getString(1)))
                    idsList.add(alarms.getString(0))
                }
                alarmrecylerView.apply {
                    layoutManager = LinearLayoutManager(activity!!)
                    adapter = AlarmListAdapter(list)
                }

                //Toast.makeText(activity!!,"${listView.size.toString()}",Toast.LENGTH_LONG).show()

            }
        }
    }
}