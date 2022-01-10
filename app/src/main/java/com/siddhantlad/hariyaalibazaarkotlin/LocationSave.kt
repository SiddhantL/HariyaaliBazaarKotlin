
package com.siddhantlad.hariyaalibazaarkotlin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*

class LocationSave : AppCompatActivity() {
   lateinit var name: TextInputLayout
   lateinit var location: TextInputLayout
   lateinit var spinnerPC: Spinner
   var arrayList: ArrayList<String> = ArrayList<String>()
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_save)
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val appbarTV: TextView = toolbar.findViewById<View>(R.id.appbartextview) as TextView
        appbarTV.setText(R.string.app_name)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        val window: Window = getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)
        val submitTv: TextView = findViewById<TextView>(R.id.buttonConfirmTV)
        name = findViewById<TextInputLayout>(R.id.nameTV)
        location = findViewById<TextInputLayout>(R.id.locationTV)
        spinnerPC = findViewById<Spinner>(R.id.spinnerPC)
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.custom_spinner, arrayList)
        spinnerPC.setAdapter(arrayAdapter)
        arrayList.add("Pincode")
        val mData: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("LocationData").child("Pincode")
        mData.addListenerForSingleValueEvent(object : ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
                for (pcodes in snapshot.getChildren()) {
                    arrayList.add(pcodes.getValue() as String)
                    arrayAdapter.notifyDataSetChanged()
                }
            }

           override fun onCancelled(error: DatabaseError) {}
        })
        arrayAdapter.notifyDataSetChanged()
        submitTv.setOnClickListener(View.OnClickListener {
            if (validate()) {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                val profileUpdates: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getEditText()?.getText().toString()).build()
                mAuth.getCurrentUser()?.updateProfile(profileUpdates)
                val mData: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("userData").child(mAuth.currentUser!!.uid)
                mData.child("Location").setValue(location.getEditText()?.getText().toString())
                mData.child("Name").setValue(name.getEditText()?.getText().toString())
                    .addOnCompleteListener(object : OnCompleteListener<Void?> {
                       override fun onComplete(task: Task<Void?>) {
                            startActivity(Intent(this@LocationSave, MainActivity::class.java))
                            finish()
                        }
                    })
            }
        })
    }

    fun validate(): Boolean {
        var validateB = true
        if (!name.getEditText()?.getText().toString().matches("^[A-Za-z ]+$".toRegex())) {
            validateB = false
            name.getEditText()?.setError("Please Enter a Valid Name")
        }
        if (location.getEditText()?.getText().toString() == "") {
            validateB = false
            location.getEditText()?.setError("Please Enter a Valid Location")
        }
        if (spinnerPC.getSelectedItem() == null) {
            validateB = false
        } else if (spinnerPC.getSelectedItem()
                .toString() == "Pincode" || spinnerPC.getSelectedItem()
                .toString() == "Select a Valid Pincode"
        ) {
            validateB = false
            val errorText: TextView = spinnerPC.getSelectedView() as TextView
            errorText.setError("")
            errorText.setTextColor(Color.RED) //just to highlight that this is an error
            errorText.setText("Select a Valid Pincode")
        }
        return validateB
    }
}