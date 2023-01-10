package com.example.weatherapp.fragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherDashboardBinding
import com.example.weatherapp.factory.WeatherViewModelFactory
import com.example.weatherapp.model.AllDataModelClass
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*


@Suppress("DEPRECATION")
class WeatherDashboard : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var fragmentMainBinding: FragmentWeatherDashboardBinding
    private lateinit var weatherViewModel: WeatherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = WeatherViewModelFactory()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        weatherViewModel =
            ViewModelProvider(requireActivity(), factory).get(WeatherViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weather_dashboard, container, false
        )
        val view: View = fragmentMainBinding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMainBinding.rlMainLayout.visibility = View.GONE
        fragmentMainBinding.etGetCityName.setOnEditorActionListener { v, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getCityWeather(fragmentMainBinding.etGetCityName.text.toString())
                val view = requireActivity().currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    fragmentMainBinding.etGetCityName.clearFocus()
                }
                true
            } else false
        }
        if (checkForInternet(requireActivity())) {
            getCurrentLocation()
        } else {
            getFromSharedPreference()
        }

        weatherViewModel.data.observe(requireActivity()) {
            setDataOnViews(it)
            it?.let { it1 -> storeInSharedPreferences(it1) }
        }

    }

    @SuppressLint("ServiceCast")
    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                //final latitude and longitude code here
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }

                val LocationRequest = com.google.android.gms.location.LocationRequest()
                LocationRequest.interval = 5
                LocationRequest.fastestInterval = 0
                LocationRequest.numUpdates = 1

                fusedLocationProviderClient.requestLocationUpdates(
                    LocationRequest, LocationCallback,
                    Looper.myLooper()
                )


            } else {
                //settings open here
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, LOCATION_ENABLED)


            }
        } else {
            //request permission
            requestPermission()
        }
    }

    private fun getFromSharedPreference() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
        val gson = Gson()
        val json: String = sharedPreferences.getString(PREF_KEY_LAST_LOCATION, "").toString()

        val obj: AllDataModelClass = gson.fromJson(json, AllDataModelClass::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            weatherViewModel.data.value = obj
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun storeInSharedPreferences(lastLocation: AllDataModelClass) {
        val gson = Gson()
        val json = gson.toJson(lastLocation)
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PREF_KEY_LAST_LOCATION, json)
        editor.apply()

    }

    private val LocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val LastLocation = locationResult.lastLocation
            if (LastLocation == null) {
                Toast.makeText(requireActivity(), "Null Received", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //fetch the weather
                fetchCurrentLocationWeather(
                    LastLocation.latitude.toString(),
                    LastLocation.longitude.toString(),
                )
            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOCATION_ENABLED) {
            Log.d("location", "location enabled")
            getCurrentLocation()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            (LocationManager.NETWORK_PROVIDER)
        )
    }


    private fun fetchCurrentLocationWeather(latitude: String, longitude: String) {
        fragmentMainBinding.progressbar.visibility = View.VISIBLE
        weatherViewModel.getCurrentWeatherData(latitude, longitude)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCityWeather(cityName: String) {
        fragmentMainBinding.progressbar.visibility = View.VISIBLE
        weatherViewModel.getCityWeatherData(cityName, API_KEY)
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setDataOnViews(body: AllDataModelClass?) {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm")
        val currentDate = sdf.format(Date())
        if (body == null) {
            return
        }
        fragmentMainBinding.tvDateAndTime.text = currentDate
        fragmentMainBinding.tvDayMaxTemp.text =
            "Day " + getTemp(body.mainData?.temp_max)
        fragmentMainBinding.tvDayMinTemp.text =
            "Night " + getTemp(body.mainData?.temp_min)
        fragmentMainBinding.tvTemp.text = "" + getTemp(body.mainData?.temp)
        fragmentMainBinding.tvFeelsLike.text =
            "Feels like " + getTemp(body.mainData?.feels_like)
        fragmentMainBinding.tvWeatherType.text = body.weatherData?.get(0)?.main ?: "--"
        fragmentMainBinding.tvSunrise.text = getSunTime(body.systemData?.sunrise?.toLong())
        fragmentMainBinding.tvSunset.text = getSunTime(body.systemData?.sunset?.toLong())
        fragmentMainBinding.tvPressure.text = (body.mainData?.pressure ?: "--").toString()
        fragmentMainBinding.tvHumidity.text = (body.mainData?.humidity ?: "--").toString() + " %"
        fragmentMainBinding.tvWindSpeed.text = (body.windData?.speed ?: "--").toString() + " m/s"

        fragmentMainBinding.tvTempFarenhite.text = getTempFernhite(body.mainData?.temp)
        fragmentMainBinding.etGetCityName.setText(body.name)

        updateUI(body.weatherData?.get(0)?.id ?: 0)

    }

    private fun getTemp(temp: Double?): String {
        if (temp == null) {
            return "--"
        }
        return "${kelvinToCelsius(temp)}°"
    }

    private fun getTempFernhite(Fernhite: Double?): String {
        if (Fernhite == null) {
            return "--"
        }
        return "${String.format("%.2f", (kelvinToCelsius(Fernhite)).times(1.8).plus(32))}°"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSunTime(Sun: Long?): String {
        if (Sun == null) {
            return "--"
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            "${timeStampToLocalDate(Sun.toLong())}"
        } else {
            return "--"
        }
    }

    private fun updateUI(id: Int) {
        fragmentMainBinding.progressbar.visibility = View.VISIBLE
        val window: Window = requireActivity().window
        if (id in 200..232) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.thunderstorm)
            fragmentMainBinding.rlToolbar.setBackgroundColor(resources.getColor(R.color.thunderstorm))
            fragmentMainBinding.rlSubLayout.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.thunderstrom_bg)
            fragmentMainBinding.llMainBgBelow.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.thunderstrom_bg)
            fragmentMainBinding.llMainBgAbove.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.thunderstrom_bg)
            fragmentMainBinding.ivWeatherBg.setImageResource(R.drawable.thunderstrom_bg)
            fragmentMainBinding.ivWeatherIcon.setImageResource(R.drawable.thunderstorm)
        } else if (id in 300..321) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.drizzle)
            fragmentMainBinding.rlToolbar.setBackgroundColor(resources.getColor(R.color.drizzle))
            fragmentMainBinding.rlSubLayout.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.drizzle_bg)
            fragmentMainBinding.llMainBgBelow.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.drizzle_bg)
            fragmentMainBinding.llMainBgAbove.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.drizzle_bg)
            fragmentMainBinding.ivWeatherBg.setImageResource(R.drawable.drizzle_bg)
            fragmentMainBinding.ivWeatherIcon.setImageResource(R.drawable.drizzle)
        } else if (id in 500..531) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.rain)
            fragmentMainBinding.rlToolbar.setBackgroundColor(resources.getColor(R.color.rain))
            fragmentMainBinding.rlSubLayout.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rainy_bg)
            fragmentMainBinding.llMainBgBelow.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rainy_bg)
            fragmentMainBinding.llMainBgAbove.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rainy_bg)
            fragmentMainBinding.ivWeatherBg.setImageResource(R.drawable.rainy_bg)
            fragmentMainBinding.ivWeatherIcon.setImageResource(R.drawable.rain)
        } else if (id in 600..620) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.snow)
            fragmentMainBinding.rlToolbar.setBackgroundColor(resources.getColor(R.color.snow))
            fragmentMainBinding.rlSubLayout.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.snow_bg)
            fragmentMainBinding.llMainBgBelow.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.snow_bg)
            fragmentMainBinding.llMainBgAbove.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.snow_bg)
            fragmentMainBinding.ivWeatherBg.setImageResource(R.drawable.snow_bg)
            fragmentMainBinding.ivWeatherIcon.setImageResource(R.drawable.snow)
        } else if (id in 701..781) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.atmosphere)
            fragmentMainBinding.rlToolbar.setBackgroundColor(resources.getColor(R.color.atmosphere))
            fragmentMainBinding.rlSubLayout.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.mist_bg)
            fragmentMainBinding.llMainBgBelow.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.mist_bg)
            fragmentMainBinding.llMainBgAbove.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.mist_bg)
            fragmentMainBinding.ivWeatherBg.setImageResource(R.drawable.mist_bg)
            fragmentMainBinding.ivWeatherIcon.setImageResource(R.drawable.mist)
        } else if (id == 800) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.clear)
            fragmentMainBinding.rlToolbar.setBackgroundColor(resources.getColor(R.color.clear))
            fragmentMainBinding.rlSubLayout.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.clear_bg)
            fragmentMainBinding.llMainBgBelow.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.clear_bg)
            fragmentMainBinding.llMainBgAbove.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.clear_bg)
            fragmentMainBinding.ivWeatherBg.setImageResource(R.drawable.clear_bg)
            fragmentMainBinding.ivWeatherIcon.setImageResource(R.drawable.sun)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.clouds)
            fragmentMainBinding.rlToolbar.setBackgroundColor(resources.getColor(R.color.clouds))
            fragmentMainBinding.rlSubLayout.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.clouds_bg)
            fragmentMainBinding.llMainBgBelow.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.clouds_bg)
            fragmentMainBinding.llMainBgAbove.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.clouds_bg)
            fragmentMainBinding.ivWeatherBg.setImageResource(R.drawable.clouds_bg)
            fragmentMainBinding.ivWeatherIcon.setImageResource(R.drawable.clouds)
        }

        fragmentMainBinding.progressbar.visibility = View.GONE
        fragmentMainBinding.rlMainLayout.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeStampToLocalDate(timeStamp: Long): String {
        val localTime = timeStamp.let {
            Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalTime()
        }

        return localTime.toString()
    }

    private fun kelvinToCelsius(tempMax: Double): Double {
        var intTemp = tempMax
        intTemp = intTemp.minus(273)
        return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }


    companion object {
        const val API_KEY = "dab3af44de7d24ae7ff86549334e45bd"
        const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        const val LOCATION_ENABLED = 101
        const val PREF_KEY_LAST_LOCATION = "LastLocation"
    }


}