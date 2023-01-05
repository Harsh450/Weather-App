package com.example.weatherapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherDashboardBinding
import com.example.weatherapp.fragment.WeatherDashboard
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment: Fragment
        fragment = WeatherDashboard()
        loadFragment(fragment)

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

//        getCurrentLocation()
    }

    private fun loadFragment(fragment: WeatherDashboard) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

//    private fun getCurrentLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                //final latitude and longitude code here
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    requestPermission()
//                    return
//                }
//                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
//                    val location: Location? = task.result
//                    if (location == null) {
//                        Toast.makeText(this, "Null Received", Toast.LENGTH_SHORT).show()
//                    } else {
//                        //fetch the weather
//                        fetchCurrentLocationWeather(location.latitude.toString(),location.longitude.toString())
//                    }
//                }
//
//            } else {
//                //settings open here
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//
//            }
//        } else {
//            //request permission
//            requestPermission()
//
//        }
//    }
//
//    private fun fetchCurrentLocationWeather(latitude: String, longitude: String) {
//
//    }
//
//    private fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager =
//            getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            (LocationManager.NETWORK_PROVIDER)
//        )
//    }
//
//
//    companion object {
//        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
//    }
//
//    private fun checkPermissions(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            this, arrayOf(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ),
//            PERMISSION_REQUEST_ACCESS_LOCATION
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
//                getCurrentLocation()
//            } else {
//                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


}


