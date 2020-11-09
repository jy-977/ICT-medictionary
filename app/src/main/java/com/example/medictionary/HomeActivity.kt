package com.example.medictionary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.medictionary.fragments.NotificationFragment
import com.example.medictionary.fragments.PillBoxFragment
import com.example.medictionary.fragments.SearchFragment
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*


enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {
    private val notificationFragment= NotificationFragment()
    private val searchFragment=SearchFragment()
    private val pillBoxFragment=PillBoxFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        replaceFragment(searchFragment)
        bottm_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_notification ->replaceFragment (notificationFragment)
                R.id.ic_search ->replaceFragment (searchFragment)
                R.id.ic_pillbox ->replaceFragment (pillBoxFragment)
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
        if(fragment!=null){
            val transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()
        }
    }


}