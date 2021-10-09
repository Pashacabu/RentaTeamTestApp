package com.pashacabu.rentateamtestapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pashacabu.rentateamtestapp.view.aboutScreen.AboutFragment
import com.pashacabu.rentateamtestapp.view.listScreen.UsersListFragment

class MyFragmentAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val array: Array<String>
) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return array.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UsersListFragment()
            else -> AboutFragment()
        }
    }
}