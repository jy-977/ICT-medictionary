package com.example.medictionary


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.example.medictionary.model.Medicine
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.row.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchlist)
        var bundle = intent.extras
        var type = bundle?.getString("type")
        val retrofit=Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(" https://datadiscovery.nlm.nih.gov/").build()
        val jsonPlaceholderApi=retrofit.create(JsonPlaceholderApi::class.java)
        var listView = findViewById<ListView>(R.id.custom_Lis_tView)
        var list = mutableListOf<Model>()
        var idsList = mutableListOf<String>()
        if(type=="byName") {
            var name = bundle?.getString("name")
            val namesarray= arrayListOf<String>(name.toString().capitalize(),name.toString().toUpperCase(),name.toString().toLowerCase())
            for (name in namesarray){
                val myCall: Call<List<Medicine>> = jsonPlaceholderApi.getMedicinesByName(name)
                getRowsAPI(myCall,list,idsList,listView)
            }

        }
        else{

            var color = bundle?.getString("color")
            var shape = bundle?.getString("shape")
            var code = bundle?.getString("code")
            if (code.toString().isEmpty()) {
                val myCall: Call<List<Medicine>> = jsonPlaceholderApi.getMedicinesByCahrWithoutCode(shape.toString(), color.toString())
                getRowsAPI(myCall,list,idsList,listView)
            }else{
                val myCall: Call<List<Medicine>> = jsonPlaceholderApi.getMedicinesByCahr(shape.toString(), color.toString(), code.toString())
                getRowsAPI(myCall,list,idsList,listView)
            }



        }

        listView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val intent = Intent(this, PillInfoActivity::class.java).apply {
                putExtra("itemId", idsList[position].toString())
        }
            startActivity(intent)


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
    private  fun getRowsAPI(myCall: Call<List<Medicine>>,list:MutableList<Model>,idsList:MutableList<String>,listView:ListView){
        myCall.enqueue(object : Callback<List<Medicine>> {
            override fun onResponse(
                    call: Call<List<Medicine>>,
                    response: Response<List<Medicine>>
            ) {

                val Medicines: List<Medicine> = response.body()!!
                for (med in Medicines) {

                    if (med.has_image == "True")
                        list.add(Model(med.medicine_name, med.spl_strength, med.splimage.toString()))
                    else
                        list.add(Model(med.medicine_name, med.spl_strength, ""))
                    idsList.add(med.id)

                }
                listView.adapter = ListAdapter(this@SearchListActivity, R.layout.row, list)
                if(list.size==0)
                    showAlert("No results")
            }
            override fun onFailure(call: Call<List<Medicine>>, t: Throwable) {
                Toast.makeText(
                        this@SearchListActivity,
                        "Error: ${t.message.toString()}",
                        Toast.LENGTH_LONG
                ).show()
            }

        })
    }


}

