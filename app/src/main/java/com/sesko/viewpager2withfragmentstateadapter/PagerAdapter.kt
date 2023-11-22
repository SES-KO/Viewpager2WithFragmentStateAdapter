package com.sesko.viewpager2withfragmentstateadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = ArrayList<Fragment>()
    private val pageIds = fragmentList.map { it.hashCode().toLong() }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyItemChanged(itemCount -1)
    }

    fun removeFragment(fragment: Fragment) {
        val position = getPosition(fragment)
        fragmentList.remove(fragment)
        notifyItemRemoved(position)
    }

    fun getPosition(fragment: Fragment): Int {
        return fragmentList.indexOf(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun getItemId(position: Int): Long {
        return fragmentList[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return pageIds.contains(itemId)
    }
}