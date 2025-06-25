package com.example.weatherapptask_1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.example.weatherapptask_1.MainActivity
import com.example.weatherapptask_1.R
import com.example.weatherapptask_1.adapter.ViewPagerAdapter
import com.example.weatherapptask_1.databinding.ActivityOnBoardingBinding
import com.example.weatherapptask_1.model.OnboardingItem
import com.example.weatherapptask_1.utils.SharedPrefHelper

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnBoardingBinding
    private lateinit var onboardingItems: List<OnboardingItem>
    private lateinit var adapter: ViewPagerAdapter
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        // Check if already logged in
        if (SharedPrefHelper.isLoggedIn(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onboardingItems = listOf(
            OnboardingItem(
                R.drawable.cloud,
                "Welcome to WeatherApp",
                "Track weather conditions in real-time across cities worldwide."
            ),
            OnboardingItem(
                R.drawable.ic_sunset,
                "Smart Features",
                "View temperature, humidity, wind speed, sunrise/sunset & more at a glance."
            ),
            OnboardingItem(
                R.drawable.ic_launcher_foreground,
                "Get Started",
                "Search any city and stay updated with local weather — anytime, anywhere!"
            )
        )


        adapter = ViewPagerAdapter(onboardingItems)
        binding.imageSlider.adapter = adapter

        addDotsIndicator(0)
        binding.imageSlider.addOnPageChangeListener(pageChangeListener)

        // Skip
        binding.btnSkip.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        //Next
        binding.btnNext.setOnClickListener {
            if (currentPage < onboardingItems.lastIndex) {
                binding.imageSlider.currentItem = currentPage + 1
            } else {
                // Go to Login Activity
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }

        // Previous
        binding.btnBack.setOnClickListener {
            if (currentPage > 0) {
                binding.imageSlider.currentItem = currentPage - 1
            }
        }

    }
    private fun addDotsIndicator(position: Int) {
        binding.dotLayout.removeAllViews()
        val dots = Array(onboardingItems.size) {
            TextView(this).apply {
                text = "•"
                textSize = 35f
                setTextColor(
                    if (it == position) ContextCompat.getColor(this@OnBoardingActivity, R.color.color1)
                    else ContextCompat.getColor(this@OnBoardingActivity, R.color.black)
                )
                binding.dotLayout.addView(this)
            }
        }
    }

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            currentPage = position
            addDotsIndicator(position)
        }
        override fun onPageScrollStateChanged(state: Int) {}
    }
}