package com.example.medictionary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.medictionary.extra.JsonPlaceholderApi
import com.example.medictionary.models.Medicine
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PillInfoActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pillinfo)
        var bundle = intent.extras
        var id = bundle?.getString("itemId")
        var setalarm = findViewById<View>(R.id.setAlarm)as Button
        var nameTxt = findViewById<View>(R.id.medicineName)as TextView
        var description = findViewById<View>(R.id.description)as TextView
        var imprint = findViewById<View>(R.id.imprint)as TextView
        var shape = findViewById<View>(R.id.shape)as TextView
        var color = findViewById<View>(R.id.color)as TextView
        var supplier = findViewById<View>(R.id.supplier)as TextView
        var size = findViewById<View>(R.id.size)as TextView
        var inactiveIngredients = findViewById<View>(R.id.inactiveIngredients)as TextView
        var strength = findViewById<View>(R.id.strength)as TextView
        var ingredients = findViewById<View>(R.id.ingredients)as TextView
        val imageView: ImageView = findViewById(R.id.person)as ImageView
        var medName=""

            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(" https://datadiscovery.nlm.nih.gov/").build()
            val jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi::class.java)
            val myCall: Call<List<Medicine>> = jsonPlaceholderApi.getMedicinesById(id.toString())

            myCall.enqueue(object : Callback<List<Medicine>> {
                override fun onResponse(call: Call<List<Medicine>>, response: Response<List<Medicine>>) {
                    val Medicine = response.body()
                    if (Medicine != null) {
                        for (details in Medicine){
                            medName=details.medicine_name
                            nameTxt.text=details.medicine_name
                            Glide.with(this@PillInfoActivity).load("https://pillbox.nlm.nih.gov/assets/pills/large/"+details.splimage+".jpg").placeholder(R.drawable.download).into(imageView)
                            val content="Pill is with imprint " +details.splimprint+", "+details.splcolor_text.toLowerCase()+ " color and "+ details.splshape_text +" shape. It has been identified as "+details.rxstring+". It is supplied by "+ details.author +" corporation."
                            description.text=content
                            imprint.text=details.splimprint.toLowerCase().capitalize()
                            shape.text=details.splshape_text.toLowerCase().capitalize()
                            color.text=details.splcolor_text.toLowerCase().capitalize()
                            strength.text=details.spl_strength.toLowerCase().capitalize()
                            size.text=details.splsize.toLowerCase().capitalize()+" mm"
                            supplier.text=details.author.toLowerCase().capitalize()+" corporation"
                            ingredients.text=details.spl_ingredients.toLowerCase().capitalize()
                            inactiveIngredients.text=details.spl_inactive_ing.toLowerCase().capitalize()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Medicine>>, t: Throwable) {
                    Toast.makeText(this@PillInfoActivity, "${t.message.toString()}", Toast.LENGTH_LONG).show()
                }

            })
            setalarm.setOnClickListener {
                val intent = Intent(this, SetAlarmActivity::class.java).apply {
                    putExtra("medId", id.toString())
                    putExtra("medName", medName)
                }
                startActivity(intent)
            }
    }
}



