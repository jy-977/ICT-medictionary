package com.example.medictionary.extra

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.medictionary.models.AlarmModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class DBHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    val FBdb = Firebase.firestore
    companion object {

       private val DATABASE_NAME = "PillBoxDatabase"
       private val TABLE_NAME = "Alarm"
       private val Alarm_ID = "Alarm_ID"
       private val Time_taking_pill = "Time_taking_pill"
       private val Name = "Name"
       private val Status = "Status"
       private val Pill_ID = "Pill_ID"
       private val Total_daily_amount = "Total_daily_amount"
       private val Treatment_length = "Treatment_length"
       private val Hours_per_dose = "Hours_per_dose"
        private val User_id = "User_id"
   }

   override fun onCreate(db: SQLiteDatabase?) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
       //creating table with fields
       val CREATE_TABLE = ("CREATE TABLE  $TABLE_NAME ($Alarm_ID TEXT PRIMARY KEY ,$Time_taking_pill  TEXT,$Name TEXT, $Pill_ID TEXT,$Total_daily_amount INTEGER,$Treatment_length INTEGER,$Hours_per_dose INTEGER,$Status TEXT,$User_id TEXT)")
       db?.execSQL(CREATE_TABLE)

   }

   override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
       onCreate(db)
   }


   fun addAlarm(alarm_ID: String, time_taking_pill: String, name: String, pill_ID: String, total_daily_amount: Int, treatment_length: Int, hours_per_dose: Int, status: String, userId: String){
       val db = this.writableDatabase
       val contentValues = ContentValues()
       contentValues.put(Alarm_ID, alarm_ID)
       contentValues.put(Time_taking_pill, time_taking_pill)
       contentValues.put(Name, name)
       contentValues.put(Pill_ID, pill_ID)
       contentValues.put(Total_daily_amount, total_daily_amount)
       contentValues.put(Treatment_length, treatment_length)
       contentValues.put(Hours_per_dose, hours_per_dose)
       contentValues.put(Status, status)
       contentValues.put(User_id, userId)
       val success = db.insert(TABLE_NAME, null, contentValues)

   }
    fun getAlarms(user_id: String): List<AlarmModel> {
        var alarmsList = mutableListOf<AlarmModel>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $User_id = '$user_id'"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val alarm = AlarmModel(cursor.getString(cursor.getColumnIndex(Name)), cursor.getString(cursor.getColumnIndex(Time_taking_pill)))
                    alarmsList.add(alarm)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return alarmsList
    }
fun restoreAlarms(user_id: String){
    FBdb.collection("Alarms")
            .whereEqualTo("user_Id", user_id).addSnapshotListener { value, e ->
                for (document in value!!) {
                    try {
                        addAlarm(document.id, document.data.get("time_taking_pill") as String, document.data.get("name") as String, document.data.get("pill_ID") as String, document.data.get("total_daily_amount").toString().toInt() , document.data.get("treatment_length").toString().toInt(), document.data.get("hours_per_dose").toString().toInt(), document.data.get("status") as String, document.data.get("user_Id") as String)
                    }catch (ex: Exception){
                        Log.d(TAG, "${ex}")
                    }
                    Log.d(TAG, "${document.id} => ${document.data.get("user_Id")}")
                }
            }

}


}


