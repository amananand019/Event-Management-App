package com.devil.premises.unicorn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_admin_home.*

class AdminHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        adminChipBottomNavMenu.setItemSelected(R.id.bottom_nav_home, true)
        supportFragmentManager.beginTransaction().replace(R.id.adminHomeFragmentContainer, HomeFragment()).commit()
        bottomMenu()
    }

    private fun bottomMenu() {
        adminChipBottomNavMenu.setOnItemSelectedListener {
            val fragment: Fragment = when(it){
                R.id.button_nav_add -> AddPostFragment()
                R.id.bottom_nav_home -> HomeFragment()
                R.id.bottom_nav_history -> HistoryFragment()
                R.id.bottom_nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.adminHomeFragmentContainer, fragment).commit()
        }
    }
}