package com.example.medictionary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class SearchEngineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchengine)
        val bundle = intent.extras
        var email = bundle?.getString("email")
        var provider = bundle?.getString("provider")
        val db = FirebaseFirestore.getInstance()
        val shapesRef = db.collection("medicines")
        val shapeSpinner = findViewById<View>(R.id.shapeSpinner) as Spinner
        val colorSpinner = findViewById<View>(R.id.colorSpinner) as Spinner
        val shapes: MutableList<String?> = ArrayList()
        val colors: MutableList<String?> = ArrayList()

        val shapeAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item,shapes)
        shapeAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item)
        shapeSpinner.adapter = shapeAdapter

        val colorAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item,colors)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = colorAdapter

        shapesRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    if (!shapes.contains(document.getString("shape")))
                        shapes.add(document.getString("shape"))
                    if (!colors.contains(document.getString("color")))
                        colors.add(document.getString("color"))
                }
                shapeAdapter.notifyDataSetChanged()
                colorAdapter.notifyDataSetChanged()
            }
        }

        val searchByNameBtn = findViewById<View>(R.id.searchNameBtn) as Button
        val nameEt = findViewById<View>(R.id.nameEt) as EditText
        searchByNameBtn.setOnClickListener {

                val homeIntent = Intent(this, SearchListActivity::class.java).apply {
                    putExtra("type","byName")
                    putExtra("name", nameEt.text.toString())
                }
                startActivity(homeIntent)

        }

        val searchByCharBtn = findViewById<View>(R.id.searchCharBtn) as Button
        val codeTxt = findViewById<View>(R.id.codeEt) as TextView
        searchByCharBtn.setOnClickListener {
            val homeIntent = Intent(this, SearchListActivity::class.java).apply {
                putExtra("type","byChar")
                putExtra("color", colorSpinner.selectedItem.toString())
                putExtra("shape", shapeSpinner.selectedItem.toString())
                putExtra("code", codeTxt.text.toString())
            }
            startActivity(homeIntent)
        }

    }

}