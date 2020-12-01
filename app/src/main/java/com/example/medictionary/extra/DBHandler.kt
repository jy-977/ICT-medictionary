package com.example.medictionary.extra

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    companion object {

       private val DATABASE_NAME = "PillBoxDatabase"
       private val TABLE_CONTACTS = "Alarm"
       private val Alarm_ID = "Alarm_ID"
       private val Time_taking_pill = "Time_taking_pill"
       private val Name = "Name"
       private val Status = "Status"
       private val Pill_ID = "Pill_ID"
       private val Total_daily_amount = "Total_daily_amount"
       private val Treatment_length = "Treatment_length"
       private val Hours_per_dose = "Hours_per_dose"
   }

   override fun onCreate(db: SQLiteDatabase?) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
       //creating table with fields
       val CREATE_CONTACTS_TABLE = ("CREATE TABLE  $TABLE_CONTACTS ($Alarm_ID INTEGER PRIMARY KEY AUTOINCREMENT,$Time_taking_pill  TEXT,$Name TEXT, $Pill_ID TEXT,$Total_daily_amount INTEGER,$Treatment_length INTEGER,$Hours_per_dose INTEGER,$Status TEXT)")
       db?.execSQL(CREATE_CONTACTS_TABLE)

   }

   override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
       onCreate(db)
   }


   fun addAlarm(time_taking_pill:String,name:String,pill_ID:String,total_daily_amount:Int,treatment_length:Int,hours_per_dose:Int,status:String){
       val db = this.writableDatabase
       val contentValues = ContentValues()
       contentValues.put(Time_taking_pill,time_taking_pill)
       contentValues.put(Name,name)
       contentValues.put(Pill_ID,pill_ID)
       contentValues.put(Total_daily_amount,total_daily_amount)
       contentValues.put(Treatment_length, treatment_length)
       contentValues.put(Hours_per_dose, hours_per_dose)
       contentValues.put(Status,status)
       val success = db.insert(TABLE_CONTACTS, null, contentValues)

   }
    val getAlarms:Cursor
    get(){
        val db=this.writableDatabase
        val res=db.rawQuery("SELECT * FROM "+ TABLE_CONTACTS,null)
        return res
    }



}

