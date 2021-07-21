package com.devil.premises.unicorn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

    override fun onStart() {
        val mAuth = FirebaseAuth.getInstance().currentUser
        val users = FirebaseDatabase.getInstance().getReference("users")
        if (mAuth != null) {
            users.child(mAuth.uid).get().addOnSuccessListener {
                Log.i("TAG", "onStart: Got value ${it.value}")
                val user = it.child("user").value
                Log.i("TAG", "onStart: $user")

                if(user == "admin"){
                    startActivity(Intent(this,AdminHomeActivity::class.java))
                    finish()
                }else{

                    startActivity(Intent(this,StudentHomeActivity::class.java))
                    finish()
                }
            }
        }
        super.onStart()
    }
}