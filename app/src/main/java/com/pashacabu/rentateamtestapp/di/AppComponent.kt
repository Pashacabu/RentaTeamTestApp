package com.pashacabu.rentateamtestapp.di

import com.pashacabu.rentateamtestapp.MainActivity
import com.pashacabu.rentateamtestapp.view.listScreen.UsersListFragment
import dagger.Component

@Component
interface AppComponent {

    fun inject(activity : MainActivity)

    fun inject(list : UsersListFragment)
}