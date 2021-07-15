package com.devil.premises.unicorn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        chipBottomNavMenu.setItemSelected(R.id.bottom_nav_home, true)
        supportFragmentManager.beginTransaction().replace(R.id.homeFragmentContainer, HomeFragment()).commit()
        bottomMenu()
    }

    private fun bottomMenu() {
        chipBottomNavMenu.setOnItemSelectedListener {
            val fragment: Fragment = when(it){
                R.id.bottom_nav_home -> HomeFragment()
                R.id.bottom_nav_history -> HistoryFragment()
                R.id.bottom_nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.homeFragmentContainer, fragment).commit()
        }
    }
}