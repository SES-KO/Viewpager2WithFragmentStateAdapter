package com.sesko.viewpager2withfragmentstateadapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


private const val ARG_PARAM1 = "Fragment UUID"

class FragmentB : Fragment() {
    private var uuidString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuidString = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_b, container, false)

        val uuid = view.findViewById(R.id.content) as TextView
        uuid.text = uuidString

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_b, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove_this_fragment -> {
                MainActivity.pagerAdapter!!.removeFragment(this)
                // show most right page
                MainActivity.viewPager!!.currentItem = MainActivity.pagerAdapter!!.itemCount-1
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(uuidString: String) =
            FragmentB().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, uuidString)
                }
            }
    }
}