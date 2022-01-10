package com.siddhantlad.hariyaalibazaarkotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    var locExist: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windows: Window = getWindow()
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        windows.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)
        FirebaseApp.initializeApp(this)
        Handler().postDelayed({ startApp() }, 2500)
    }

    fun startApp() {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        locExist = false
        if (mAuth.getCurrentUser() != null) {
            if (!mAuth.getCurrentUser()!!.getDisplayName().equals("")) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity,LocationSave::class.java))
                finish()
            }
        } else {
            startActivity(Intent(this@SplashActivity, NumberVerification::class.java))
            finish()
        }


    }
}