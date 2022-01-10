package com.siddhantlad.hariyaalibazaarkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {
    lateinit var verificationId: String
    lateinit var mAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)
        val window: Window = getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById<ProgressBar>(R.id.progressbar)
        editText = findViewById<EditText>(R.id.editTextCode)
        val phonenumber: String? = getIntent().getStringExtra("phonenumber")
        if (phonenumber != null) {
            sendVerificationCode(phonenumber)
        }
        findViewById<View>(R.id.buttonSignIn).setOnClickListener(View.OnClickListener {
            val code: String = editText.getText().toString().trim { it <= ' ' }
            if (code.isEmpty() || code.length < 6) {
                editText.setError("Code cannot be empty")
                editText.requestFocus()
                return@OnClickListener
            }
            verifyCode(code)
        })
    }

    private fun verifyCode(code: String) {
        try {
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationId, code)
            signInWithCredential(credential)
        } catch (e: Exception) {
            Toast.makeText(this, "A Problem Occurred", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        //LOCATION
                        val intent = Intent(this@VerifyPhoneActivity, LocationSave::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@VerifyPhoneActivity,
                            task.getException()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun sendVerificationCode(number: String) {
        progressBar?.setVisibility(View.VISIBLE)
        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBack).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
           override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationId = p0
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code: String? = phoneAuthCredential.getSmsCode()
                if (code != null) {
                    editText.setText(code)
                    verifyCode(code)
                }
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(
                    this@VerifyPhoneActivity,
                    "Please Select Browser for Verification",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
}