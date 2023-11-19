package com.sesko.viewpager2withfragmentstateadapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {
    var pagerAdapter: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // overwrite appbar title
        this.title = "My title"

        val viewPager: ViewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter!!.addFragment(FragmentA())
        pagerAdapter!!.addFragment(FragmentB())
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                invalidateOptionsMenu(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        invalidateOptionsMenu(0)
    }

    private fun invalidateOptionsMenu(position: Int) {
        for (i in 0 until (pagerAdapter?.itemCount ?: 0)) {
            val fragment: Fragment? = pagerAdapter?.createFragment(i)
            fragment?.setHasOptionsMenu(i == position)
        }
        invalidateOptionsMenu()
    }
}