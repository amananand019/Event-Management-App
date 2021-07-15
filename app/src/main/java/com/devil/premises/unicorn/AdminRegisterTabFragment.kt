package com.devil.premises.unicorn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_register_tab_fragment.*

class AdminRegisterTabFragment: Fragment(R.layout.admin_register_tab_fragment) {
    val v:Float = 0F
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adminCountryCode.translationX = 800F
        adminPhoneNumber.translationX = 800F
        adminName.translationX = 800F
        adminStaffNumber.translationX = 800F
        adminRegisterBtn.translationX = 800F

        adminCountryCode.alpha = v
        adminPhoneNumber.alpha = v
        adminName.alpha = v
        adminStaffNumber.alpha = v
        adminRegisterBtn.alpha = v

        adminCountryCode.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(100).start()
        adminPhoneNumber.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(200).start()
        adminName.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(300).start()
        adminStaffNumber.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(400).start()
        adminRegisterBtn.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500).start()
    }
}