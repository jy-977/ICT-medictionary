package com.example.medictionary.models

data class ALarmModelFB(val time_taking_pill:String,val name:String,val pill_ID:String,val total_daily_amount:Int,val treatment_length:Int,val hours_per_dose:Int,val status:String,val user_Id:String)