package com.devil.premises.unicorn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        registerTabLayout.addTab(registerTabLayout.newTab().setText("Student Login"))
//        registerTabLayout.addTab(registerTabLayout.newTab().setText("Admin Login"))
        registerTabLayout.tabGravity = TabLayout.GRAVITY_FILL

        registerTabLayout.translationX = 800F
        registerTabLayout.alpha = 0F
        registerTabLayout.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(100).start()

        val fragments: ArrayList<Fragment> = arrayListOf(
            StudentRegisterTabFragment(),
            AdminRegisterTabFragment()
        )

        val adapter = RegisterViewPagerAdapter(fragments, this)
        viewPager.adapter = adapter

        TabLayoutMediator(registerTabLayout, viewPager){ tab, position ->
                when (position) {
                    0 -> { tab.text = "Student Login"}
                    1 -> { tab.text = "Admin Login"}
                }
            }.attach()
    }

    override fun onBackPressed() {
        if(viewPager.currentItem == 0)
            super.onBackPressed()
        else
            viewPager.currentItem = viewPager.currentItem - 1
    }
}