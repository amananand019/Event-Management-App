package com.devil.premises.unicorn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val users = Firebase.database.reference.child("users")
        FirebaseAuth.getInstance().currentUser?.uid?.let { users.child(it).get().addOnSuccessListener { it1 ->
            name.text = it1.child("name").value.toString()
            phone.text = it1.child("phone").value.toString()
            profileId.text = it1.child("id").value.toString()
            user.text = it1.child("user").value.toString()
        } }


        logoutBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, RegisterActivity::class.java))
            this.activity?.finish()
        }



    }
}