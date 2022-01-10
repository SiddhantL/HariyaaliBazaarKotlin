package com.siddhantlad.hariyaalibazaarkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class NumberVerification : AppCompatActivity() {
    lateinit var spinner: Spinner
    lateinit var editText: EditText
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_verification)
        val window: Window = getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)
        spinner = findViewById<Spinner>(R.id.spinnerCountries)
        spinner.setAdapter(
            ArrayAdapter<String>(
                this,
                R.layout.simplespinner,
                CountryData.countryNames
            )
        )
        editText = findViewById<EditText>(R.id.editTextPhone)
        findViewById<View>(R.id.buttonContinue).setOnClickListener(View.OnClickListener {
            val code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()]
            val number: String = editText.getText().toString().trim { it <= ' ' }
            if (number.isEmpty() || number.length < 10) {
                editText.setError("Valid number is required")
                editText.requestFocus()
                return@OnClickListener
            }
            val phoneNumber = "+$code$number"
            val intent = Intent(this@NumberVerification, VerifyPhoneActivity::class.java)
            intent.putExtra("phonenumber", phoneNumber)
            startActivity(intent)
        })
    }

    protected override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
           /* val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)*/
            Toast.makeText(this,"Yay",Toast.LENGTH_LONG).show()
        }
    }
}