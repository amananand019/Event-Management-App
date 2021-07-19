package com.devil.premises.unicorn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_phone_auth.*
import java.util.concurrent.TimeUnit

private const val TAG = "PhoneAuthActivity"

class PhoneAuthActivity : AppCompatActivity() {
    private lateinit var mVerificationBySystem: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var user: String
    private lateinit var id: String
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        otpEditText.isEnabled = false
        authenticateBtn.isEnabled = false

        val bundle = intent.extras
        phone = bundle!!.getString("phone", "default")
        name = bundle.getString("name", "default")
        id = bundle.getString("id", "default")
        user = bundle.getString("user", "default")

        mAuth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().getReference("users")

        sendVerificationCodeToUser(phone)

        authenticateBtn.setOnClickListener{
            val verificationCode = otpEditText.text.toString()
            linearProgressIndicator.visibility = View.VISIBLE
            if(verificationCode == ""){
                Toast.makeText(this,"Please Enter the OTP first..", Toast.LENGTH_SHORT).show()
            }else{
                val credential = PhoneAuthProvider.getCredential(mVerificationBySystem, verificationCode)
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    private fun sendVerificationCodeToUser(phoneNumber: String?) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber.toString())       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d(TAG, "onVerificationFailed: "+ p0.message)
            linearProgressIndicator.visibility = View.INVISIBLE
            Toast.makeText(this@PhoneAuthActivity, p0.message, Toast.LENGTH_LONG).show()
            finish()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            mVerificationBySystem = p0
            mResendingToken = p1
            otpEditText.isEnabled = true
            authenticateBtn.isEnabled = true
            linearProgressIndicator.visibility = View.INVISIBLE
            Toast.makeText(this@PhoneAuthActivity, "Enter OTP send to your number", Toast.LENGTH_LONG).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d(TAG, "signInWithPhoneAuthCredential: Success")
                    linearProgressIndicator.visibility = View.INVISIBLE
                    sendUserToHomeActivity()
                    Toast.makeText(this, "Congrats, You have logged in.", Toast.LENGTH_SHORT).show()
                }else{
                    linearProgressIndicator.visibility = View.INVISIBLE
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendUserToHomeActivity() {
        val map = mutableMapOf<String, String>()
        map["phone"] = phone
        map["name"] = name
        map["id"] = id
        map["user"] = user

        FirebaseAuth.getInstance().currentUser?.let {
            userRef.child(it.uid)
                .setValue(map)
                .addOnCompleteListener{
                    Log.i("Firebase Child Added", "sendUserToHomeActivity: $map")
                }
        }

        if(user == "admin"){
            val intent = Intent(this, AdminHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, StudentHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }
}