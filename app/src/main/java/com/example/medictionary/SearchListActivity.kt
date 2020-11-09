package com.example.medictionary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class SearchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchlist)
        val db = FirebaseFirestore.getInstance()
        val bundle = intent.extras
        val type = bundle?.getString("type")
        val listView = findViewById<ListView>(R.id.custom_Lis_tView)
        val list = mutableListOf<Model>()
        val idsList = mutableListOf<String>()

        if (type == "byName") {
            val name = bundle.getString("name")

            db.collection("medicines").whereEqualTo("name", name).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val mainName =
                            "${document.get("name").toString()} ${document.get("mg").toString()}"
                        list.add(Model(mainName, document.get("des").toString(), R.drawable.nexium))
                        idsList.add(document.get("itemID").toString())
                    }
                }
                listView.adapter = ListAdapter(this@SearchListActivity, R.layout.row, list)
            }

        }
        else{

            val color = bundle?.getString("color")
            val shape = bundle?.getString("shape")
            val code = bundle?.getString("code")
            db.collection("medicines").whereEqualTo("color", color ).whereEqualTo("shape",shape)
                .whereEqualTo("code",code).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val mainName = "${document.get("name").toString()} ${
                                document.get("mg").toString()
                            }"
                            list.add(
                                Model(
                                    mainName,
                                    document.get("des").toString(),
                                    R.drawable.nexium
                                )
                            )
                            idsList.add(document.get("itemID").toString())
                        }
                    }
                    listView.adapter = ListAdapter(this@SearchListActivity, R.layout.row, list)
                }
        }
        listView.setOnItemClickListener { _:AdapterView<*>, _:View, position:Int, _:Long ->
            val intent = Intent(this, PillInfoActivity::class.java).apply {
                putExtra("itemId", idsList[position])
            }
            startActivity(intent)
        }
    }

}