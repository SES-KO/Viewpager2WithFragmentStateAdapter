package com.sesko.viewpager2withfragmentstateadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = ArrayList<Fragment>()

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}