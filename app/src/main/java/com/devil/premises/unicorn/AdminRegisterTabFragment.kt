package com.devil.premises.unicorn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

        adminRegisterBtn.setOnClickListener {
            adminCountryCode.registerCarrierNumberEditText(adminPhoneNumber)
            val phoneNumber = "+" + adminCountryCode.fullNumber
            val name = adminName.text.toString()
            val id = adminStaffNumber.text.toString()

            if(phoneNumber.trim().isNotEmpty() && name.trim().isNotEmpty() && id.trim().isNotEmpty()){
                val bundle = Bundle()
                bundle.putString("phone", phoneNumber)
                bundle.putString("name", name)
                bundle.putString("id",id)
                bundle.putString("user", "admin")

                val ref = Firebase.database.reference.child("admins")
                ref.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.child(phoneNumber).exists()){
                            val intent = Intent(activity, PhoneAuthActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }else{
                            Toast.makeText(context, "You are not an admin, Please try using student login", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }else{
                Toast.makeText(activity, "Please fill all the details..", Toast.LENGTH_LONG).show()
            }
        }
    }
}