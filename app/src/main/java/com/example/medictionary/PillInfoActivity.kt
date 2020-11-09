package com.example.medictionary

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class PillInfoActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pillinfo)
        var bundle = intent.extras
        var id = bundle?.getString("itemId")
        var nameTxt=findViewById<View>(R.id.medicineName)as TextView
        var des=findViewById<View>(R.id.medDes)as TextView
        var sE=findViewById<View>(R.id.medse)as TextView
        db.collection("medicines").whereEqualTo("itemID", id).get().addOnCompleteListener(object :
            OnCompleteListener<QuerySnapshot?> {
            override fun onComplete(task: Task<QuerySnapshot?>) {
                if (task.isSuccessful()) {
                    for (document in task.getResult()!!) {
                        nameTxt.text=document.get("name").toString()+" "+document.get("mg").toString()
                        des.text=document.get("des").toString()
                        sE.text=document.get("sideEffects").toString()
                    }
                }
                }
            })
        }
}


