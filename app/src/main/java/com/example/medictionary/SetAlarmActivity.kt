package com.example.medictionary

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.medictionary.extra.DBHandler
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SetAlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setalarm)
        val bundle = intent.extras
        val id = bundle?.getString("medId")
        val name = bundle?.getString("medName")

            val saveBtn = findViewById<View>(R.id.setAlarmBtn) as Button
            val days = findViewById<View>(R.id.days) as EditText
            val number = findViewById<View>(R.id.number) as EditText
            val hours = findViewById<View>(R.id.hours) as EditText
            val timePicker = findViewById<View>(R.id.timepicker) as TimePicker
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            val datetime = " " + timePicker.hour.toString() + ":" + timePicker.minute.toString()
            var dbHelper= DBHandler(this)
            saveBtn.setOnClickListener {
                try { val status = "Enabled"
                    dbHelper.addAlarm(datetime,name.toString(),id.toString(),number.text.toString().toInt(),days.text.toString().toInt(),hours.text.toString().toInt(),status)
                    val res=dbHelper.getAlarms
                    showAlert("The alarm have been set succesfully")

            }catch (ex:Exception)
                {
                    ex.printStackTrace()
                    Toast.makeText(applicationContext, "${ex}", Toast.LENGTH_LONG).show()
                }
        }
}
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Saved")
        builder.setMessage(message)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
