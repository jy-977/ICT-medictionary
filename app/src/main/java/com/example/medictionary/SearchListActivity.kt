package com.example.medictionary


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.facebook.CallbackManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class SearchListActivity : AppCompatActivity() {
    val db=FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchlist)
        val db = FirebaseFirestore.getInstance()
        var bundle = intent.extras
        var type = bundle?.getString("type")
        var listView = findViewById<ListView>(R.id.custom_Lis_tView)
        var list = mutableListOf<Model>()
        var idsList = mutableListOf<String>()
        if(type=="byName") {
            var name = bundle?.getString("name")

            db.collection("medicines").whereEqualTo("name", name).get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                override fun onComplete(task: Task<QuerySnapshot?>) {
                    if (task.isSuccessful()) {
                        for (document in task.getResult()!!) {
                            val mainName = "${document.get("name").toString()} ${document.get("mg").toString()}"
                            list.add(Model(mainName, document.get("des").toString(), R.drawable.nexium))
                            idsList.add(document.get("itemID").toString())
                        }
                    }
                    listView.adapter = ListAdapter(this@SearchListActivity, R.layout.row, list)


                }
            })

        }
        else{

            var color = bundle?.getString("color")
            var shape = bundle?.getString("shape")
            var code = bundle?.getString("code")
            db.collection("medicines").whereEqualTo("color", color ).whereEqualTo("shape",shape).whereEqualTo("code",code).get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                override fun onComplete(task: Task<QuerySnapshot?>) {
                    if (task.isSuccessful()) {
                        for (document in task.getResult()!!) {
                            val mainName = "${document.get("name").toString()} ${document.get("mg").toString()}"
                            list.add(Model(mainName, document.get("des").toString(), R.drawable.nexium))
                            idsList.add(document.get("itemID").toString())
                        }
                    }
                    listView.adapter = ListAdapter(this@SearchListActivity, R.layout.row, list)
                }
            })
        }
        listView.setOnItemClickListener { parent:AdapterView<*>, view:View, position:Int, id:Long ->
            val intent = Intent(this, PillInfoActivity::class.java).apply {
                putExtra("itemId", idsList[position].toString())
        }
            startActivity(intent)


    }
    }

}