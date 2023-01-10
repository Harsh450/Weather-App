package com.example.weatherapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.weatherapp.R
import com.example.weatherapp.fragment.WeatherDashboard

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var fragment: WeatherDashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = WeatherDashboard()
        loadFragment(fragment)

    }

    private fun loadFragment(fragment: WeatherDashboard) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == WeatherDashboard.PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fragment.getCurrentLocation()
                Toast.makeText(this.applicationContext, "Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this.applicationContext, "Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}


