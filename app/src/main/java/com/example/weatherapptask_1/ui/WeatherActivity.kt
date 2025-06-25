package com.example.weatherapptask_1.ui

import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapptask_1.R
import com.example.weatherapptask_1.databinding.ActivityWeatherBinding
import com.example.weatherapptask_1.reporitory.WeatherRepository
import com.example.weatherapptask_1.utils.Constants
import com.example.weatherapptask_1.viewmodel.WeatherViewModel
import com.example.weatherapptask_1.viewmodel.WeatherViewModelFactory
import java.util.Locale
import java.util.TimeZone

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(16, systemBars.top, 16, systemBars.bottom)
            insets
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        // get intent
        val city = intent?.getStringExtra("city") ?: "Varanasi"

        val repository = WeatherRepository()
        val factory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        // Observe weather data
        viewModel.weatherData.observe(this) { weather ->
            weather?.let {
                binding.locationTv.text = "${it.name}, ${it.sys.country}"
                binding.tempTv.text = "${it.main.temp}° C"
                binding.weatherTypeTv.text = "${it.weather.firstOrNull()?.main}"
                binding.weatherDescTv.text = "${it.weather.firstOrNull()?.description}"
                binding.windTv.text = "${it.wind.speed} km/h"
                binding.pressorTv.text = "${it.main.pressure} hPa"
                binding.humidityTv.text = "${it.main.humidity}%"

                binding.sunriseTimeTv.text = convertUnixToTimeWithOffset(it.sys.sunrise, it.timezone)
                binding.sunsetTimeTv.text = convertUnixToTimeWithOffset(it.sys.sunset, it.timezone)


                // Optional: load weather icon
                val iconCode = it.weather.firstOrNull()?.icon
                 val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
                 Glide.with(this).load(iconUrl).into(binding.imageView)
            }
        }

        viewModel.error.observe(this) { error->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        // Fetch weather
        viewModel.fetchWeatherByCity(city, Constants.API_KEY)

        binding.materialCardView.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Enter City")

            val input = EditText(this).apply {
                hint = "E.g., Mumbai"
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setPadding(40, 30, 40, 30)
                textSize = 16f
                isSingleLine = true
            }

            // Wrap EditText inside LinearLayout with margin
            val container = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(40, 20, 40, 20)
                input.layoutParams = params
                addView(input)
            }

            builder.setView(container)

            builder.setPositiveButton("Search") { _, _ ->
                val newCity = input.text.toString().trim()
                if (newCity.isNotEmpty()) {
                    viewModel.fetchWeatherByCity(newCity, Constants.API_KEY)
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }

    }

    fun convertUnixToTimeWithOffset(unixTime: Long, offsetInSeconds: Int): String {
        val date = java.util.Date((unixTime + offsetInSeconds) * 1000)
        val sdf = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC") // Because we already added offset
        return sdf.format(date)
    }

}