package com.example.medictionary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.medictionary.extra.DBHandler
import com.example.medictionary.fragments.PillBoxFragment
import com.example.medictionary.fragments.SearchFragment
import com.example.medictionary.models.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*


enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {
    private val searchFragment=SearchFragment()
    private val pillBoxFragment=PillBoxFragment()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        var dbHelper = DBHandler(this)
        (dbHelper as DBHandler).restoreAlarms(email.toString())
        val user = UserModel(email.toString(), provider.toString())
        db.collection("Users")
                .document(email.toString()).set(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("DocSnippets", "DocumentSnapshot written")
                }
                .addOnFailureListener { e ->
                    Log.w("DocSnippets", "Error adding document", e)
                }
        replaceFragment(searchFragment,email.toString(),provider.toString())
        bottm_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_search -> replaceFragment(searchFragment,email.toString(),provider.toString())
                R.id.ic_pillbox -> replaceFragment(pillBoxFragment,email.toString(),provider.toString())
            }
            true
        }

        // Save data

        val prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()
    }
    private fun replaceFragment(fragment: Fragment,email:String,provider:String){
        val transaction = supportFragmentManager.beginTransaction()
        val b = Bundle()
        b.putString("email", email)
        b.putString("provider", provider)
        fragment.setArguments(b);
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}