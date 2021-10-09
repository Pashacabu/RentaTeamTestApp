package com.pashacabu.rentateamtestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pashacabu.rentateamtestapp.di.DaggerAppComponent
import com.pashacabu.rentateamtestapp.view.*
import com.pashacabu.rentateamtestapp.viewmodel.AppViewModel
import kotlinx.serialization.builtins.ByteArraySerializer

class MainActivity : AppCompatActivity() {

    val appComponent = DaggerAppComponent.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BaseFragment(), BASE_FRAGMENT_TAG)
                .commit()
        } else {
            val fragment = supportFragmentManager.findFragmentByTag(BASE_FRAGMENT_TAG)
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, BASE_FRAGMENT_TAG)
                    .commit()
            }
        }
    }

    companion object {
        const val BASE_FRAGMENT_TAG = "BaseFragment"
    }
}