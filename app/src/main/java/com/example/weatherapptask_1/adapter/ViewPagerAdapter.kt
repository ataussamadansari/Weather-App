package com.example.weatherapptask_1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.weatherapptask_1.model.OnboardingItem
import com.example.weatherapptask_1.R

class ViewPagerAdapter(private val items: List<OnboardingItem>) : PagerAdapter() {

    override fun getCount(): Int = items.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context)
            .inflate(R.layout.slider_layout, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.introImage)
        val titleView = itemView.findViewById<TextView>(R.id.introHeading)
        val descView = itemView.findViewById<TextView>(R.id.introParagraph)

        val item = items[position]
        imageView.setImageResource(item.image)
        titleView.text = item.title
        descView.text = item.desc

        container.addView(itemView)
        return itemView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean  = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}