package com.example.medictionary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.medictionary.fragments.PillBoxFragment
import com.example.medictionary.fragments.SearchFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.installations.remote.FirebaseInstallationServiceClient
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_home.*
import java.io.IOException


enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {
   // private val notificationFragment= NotificationFragment()
    private val searchFragment=SearchFragment()
    private val pillBoxFragment=PillBoxFragment()
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

      //  myToken()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
           // val msg = getString(R.string.msg_token_fmt, token)
            //Log.d(TAG, msg)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })





        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {task-> Log.i("token", task.token)}

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        replaceFragment(searchFragment)
        bottm_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
     //           R.id.ic_notification -> replaceFragment (notificationFragment)
                R.id.ic_search -> replaceFragment (searchFragment)
                R.id.ic_pillbox -> replaceFragment (pillBoxFragment)
            }
            true
        }

        // Save data

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

    }
    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }
/*
    private fun myToken(){
        Thread(Runnable {
            try {
                FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.i(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListner
                    }
                    val token = task.result?.token
                    textView.text = token
                })
            } catch (e: IOException){
                e.printStackTrace()
            }
        }).start()
    }*/
}