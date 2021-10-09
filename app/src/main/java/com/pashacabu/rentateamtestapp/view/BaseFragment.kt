package com.pashacabu.rentateamtestapp.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pashacabu.rentateamtestapp.R

class BaseFragment : Fragment(R.layout.fragment_base) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
    }

    private fun findViews(view: View) {
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        val tabs = resources.getStringArray(R.array.tabs)
        viewPager.adapter =
            MyFragmentAdapter(requireActivity().supportFragmentManager, requireActivity().lifecycle, tabs)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}