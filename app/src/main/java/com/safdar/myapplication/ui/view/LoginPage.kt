package com.safdar.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.ActivityMainBinding
import com.safdar.myapplication.databinding.LoginPageBinding
import java.util.concurrent.TimeUnit

class LoginPage : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding
    private lateinit var mAuth: FirebaseAuth
    lateinit var verificationId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_page)
        mAuth = FirebaseAuth.getInstance()
        clickListener()
    }

    private fun clickListener() {
        binding.idBtnGetOtp.setOnClickListener {
            if (TextUtils.isEmpty(binding.idEdtPhoneNumber.text.toString())) {
                toastMessage("Please Enter phone Number First")
            } else {
                binding.bar.visibility = View.VISIBLE
                sendVerificationCode()
            }

        }
        binding.idBtnVerify.setOnClickListener {
            if (TextUtils.isEmpty(binding.idEdtOtp.toString())) {
                toastMessage(binding.idEdtPhoneNumber.toString())
            } else {
                verifyCode(binding.idEdtOtp.text.toString())
            }
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signingByCredentials(credential)
    }

    private fun signingByCredentials(credential: PhoneAuthCredential) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    toastMessage("Verification is Complete")
                    startActivity(Intent(this@LoginPage, TicTacToe::class.java))
                } else {
                    toastMessage("Enter Correct otp code ")
                }
            }
    }

    private fun sendVerificationCode() {
        val phoneNumber = binding.idEdtPhoneNumber.text.toString()
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91$phoneNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        Log.i(TAG, "sendVerificationCode: $phoneNumber")
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            val code = credential.smsCode
            if (code != null) {
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            toastMessage("Otp Can't send Right Now , please try after some time  Again")

        }

        override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.d(TAG, "onCodeSent:$id")
            super.onCodeSent(id, token)
            binding.idBtnVerify.isEnabled = true
            verificationId = id
            toastMessage("Code is Sent")
            binding.bar.visibility = View.INVISIBLE
        }
    }

    private fun toastMessage(msg: String) {
        Toast.makeText(this@LoginPage, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(this@LoginPage, TicTacToe::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "Login Page :- "
    }
}
