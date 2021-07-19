package com.devil.premises.unicorn

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.student_register_tab_fragment.*

class StudentRegisterTabFragment: Fragment(R.layout.student_register_tab_fragment) {
    val v: Float = 0F
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentCountryCode.translationX = 800F
        studentPhoneNumber.translationX = 800F
        studentName.translationX = 800F
        studentUSN.translationX = 800F
        studentRegisterBtn.translationX = 800F

        studentCountryCode.alpha = v
        studentPhoneNumber.alpha = v
        studentName.alpha = v
        studentUSN.alpha = v
        studentRegisterBtn.alpha = v

        studentCountryCode.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(300).start()
        studentPhoneNumber.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(300).start()
        studentName.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(400).start()
        studentUSN.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(600).start()
        studentRegisterBtn.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(700).start()

        studentRegisterBtn.setOnClickListener {
            studentCountryCode.registerCarrierNumberEditText(studentPhoneNumber)
            val phoneNumber = "+" + studentCountryCode.fullNumber
            val name = studentName.text.toString()
            val usn = studentUSN.text.toString()

            if(phoneNumber.trim().isNotEmpty() && name.trim().isNotEmpty() && usn.trim().isNotEmpty()){
                val bundle = Bundle()
                bundle.putString("phone", phoneNumber)
                bundle.putString("name", name)
                bundle.putString("id",usn)
                bundle.putString("user", "student")

                val intent = Intent(activity, PhoneAuthActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }else{
                Toast.makeText(activity, "Please fill all the details..", Toast.LENGTH_LONG).show()
            }
        }
    }
}