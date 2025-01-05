package com.together.togetherproject

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.together.togetherproject.presentation.home.view.HomeFragment
import com.together.togetherproject.presentation.map.view.MapFragment
import com.together.togetherproject.presentation.mypage.view.MyPageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disableDarkMode()

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            handleNavigation(item)
        }

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.homeFragment
        }
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun handleNavigation(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.mapFragment -> MapFragment()
            R.id.homeFragment -> HomeFragment()
            R.id.myPageFragment -> MyPageFragment()
            else -> HomeFragment()
        }
        replaceFragment(fragment)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (currentFragment?.javaClass == fragment.javaClass) return
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commitAllowingStateLoss()
    }
}