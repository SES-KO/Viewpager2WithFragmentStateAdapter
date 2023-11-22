package com.sesko.viewpager2withfragmentstateadapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {
    companion object {
        var pagerAdapter: PagerAdapter? = null
        var viewPager: ViewPager2? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // overwrite appbar title
        this.title = "My title"

        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter!!.addFragment(FragmentA())

        viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager!!.adapter = pagerAdapter
    }
}