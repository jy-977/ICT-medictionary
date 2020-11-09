package com.example.medictionary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.lang.Exception


class SearchEngineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchengine)
        var bundle = intent.extras
        var email = bundle?.getString("email")
        var provider = bundle?.getString("provider")
        val db= FirebaseFirestore.getInstance()
        val shapesRef= db.collection("medicines")
        val shapeSpinner=findViewById<View>(R.id.shapeSpinner) as Spinner
        val colorSpinner=findViewById<View>(R.id.colorSpinner) as Spinner
        val shapes: MutableList<String?> = ArrayList()
        val colors: MutableList<String?> = ArrayList()
        val shapeAdapter= ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item,shapes)
        shapeAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item)
        shapeSpinner.adapter=shapeAdapter
        val colorAdapter= ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item,colors)
        colorAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter=colorAdapter
        shapesRef.get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?>{
            override fun onComplete(task: Task<QuerySnapshot?>) {
                if (task.isSuccessful()) {
                    for (document in task.getResult()!!) {
                        if(!shapes.contains(document.getString("shape")))
                        shapes.add(document.getString("shape"))
                        if(!colors.contains(document.getString("color")))
                        colors.add(document.getString("color"))
                    }
                    shapeAdapter.notifyDataSetChanged()
                    colorAdapter.notifyDataSetChanged()
                    }
            }
        })
        val searchByNameBtn= findViewById<View>(R.id.searchNameBtn) as Button
        val nameEt= findViewById<View>(R.id.nameEt) as EditText
        searchByNameBtn.setOnClickListener {
            if(nameEt.text.toString().trim().isNotEmpty() ||
                    nameEt.text.toString().trim().isNotBlank()) {
                    val homeIntent = Intent(this, SearchListActivity::class.java).apply {
                        putExtra("type", "byName")
                        putExtra("name", nameEt.text.toString())
                    }
                    startActivity(homeIntent)
                }
            else {
                showAlert("The searching field should not be empty! please write the medicine name")
            }

        }
        val searchByCharBtn = findViewById<View>(R.id.searchCharBtn) as Button
        val codeTxt = findViewById<View>(R.id.codeEt) as TextView
        searchByCharBtn.setOnClickListener {
            if(codeTxt.toString().trim().length>0) {
                val homeIntent = Intent(this, SearchListActivity::class.java).apply {
                    putExtra("type", "byChar")
                    putExtra("color", colorSpinner.selectedItem.toString())
                    putExtra("shape", shapeSpinner.selectedItem.toString())
                    putExtra("code", codeTxt.text.toString())
                }
                startActivity(homeIntent)
            }
            else
            showAlert("The code field should not be empty! please write the medicine code")
        }


    }
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}