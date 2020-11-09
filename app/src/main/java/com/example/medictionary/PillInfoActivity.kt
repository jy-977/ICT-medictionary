package com.example.medictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PillInfoActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pillinfo)
        var bundle = intent.extras
        var id = bundle?.getString("itemId")
        var nameTxt = findViewById<View>(R.id.medicineName)as TextView
        var des = findViewById<View>(R.id.medDes)as TextView
        var sE = findViewById<View>(R.id.medse)as TextView
        db.collection("medicines").whereEqualTo("itemID", id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    nameTxt.text = document.get("name").toString() + " " + document.get("mg").toString()
                    des.text = document.get("des").toString()
                    sE.text = document.get("sideEffects").toString()
                }
            }
        }
    }
}


