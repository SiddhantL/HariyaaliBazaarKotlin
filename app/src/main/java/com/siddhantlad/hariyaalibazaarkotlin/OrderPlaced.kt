package com.siddhantlad.hariyaalibazaarkotlin

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList
import com.google.firebase.database.*

class OrderPlaced : AppCompatActivity() {
    lateinit var vegAdapter: AdapterOrders
    lateinit var vegetablesList: ArrayList<ArrayOrderPassData>
    var veggies: ArrayOrderPassData?=null
    var vegetable = ""
    lateinit var veg: ArrayList<String>
    lateinit var vegAdapter_past: AdapterOrders
    lateinit var vegetablesList_past: ArrayList<ArrayOrderPassData>
    var veggies_past: ArrayOrderPassData?=null
    var vegetable_past = ""
    lateinit var veg_past: ArrayList<String>
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        veg= ArrayList()
        veg_past=ArrayList()
        val window: Window = getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val appbarTV: TextView = toolbar.findViewById<View>(R.id.appbartextview) as TextView
        appbarTV.setText(R.string.app_name)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.recycler)
        vegetablesList = ArrayList()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        vegAdapter = AdapterOrders(vegetablesList, this)
        recyclerView.setAdapter(vegAdapter)
        val mData: DatabaseReference = FirebaseDatabase.getInstance().getReference("currOrders")
        //String pushStr=mData.push().getKey();
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mData.addListenerForSingleValueEvent(object : ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
                for (orderDS in snapshot.getChildren()) {
                    if (mAuth.currentUser?.uid.equals(orderDS.child("User").getValue(String::class.java))) {
                        orderDS.getKey()?.let {
                            mData.child(it).child("order")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (vegetableDS in snapshot.getChildren()) {
                                            //Toast.makeText(OrderPlaced.this, vegetableDS.getKey(), Toast.LENGTH_SHORT).show();
                                            if (veggies != null) {
                                                vegetable = vegetableDS.child("Name")
                                                    .getValue(String::class.java)
                                                    .toString() + ": " + vegetableDS.child("Value")
                                                    .getValue(
                                                        String::class.java
                                                    )
                                                veg!!.add(vegetable)
                                            } else {
                                                vegetable = vegetableDS.child("Name")
                                                    .getValue(String::class.java)
                                                    .toString() + ": " + vegetableDS.child("Value")
                                                    .getValue(
                                                        String::class.java
                                                    )
                                                veg!!.add(vegetable)
                                            }
                                        }
                                        if (veg != null) {
                                            veggies = ArrayOrderPassData(veg as ArrayList<String>, "PRICE")
                                            vegetablesList!!.add(veggies!!)
                                        }
                                        vegAdapter!!.notifyDataSetChanged()
                                        //veggies="";
                                        veg!!.clear()
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        }
                    }
                }
            }

           override fun onCancelled(error: DatabaseError) {}
        })
        val recyclerView_past: RecyclerView = findViewById<RecyclerView>(R.id.recycler_past)
        vegetablesList_past = ArrayList()
        val layoutManager_past = LinearLayoutManager(this)
        recyclerView_past.setLayoutManager(layoutManager_past)
        vegAdapter_past = AdapterOrders(vegetablesList_past, this)
        recyclerView_past.setAdapter(vegAdapter_past)
        val mData_past: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("pastOrders")
        //String pushStr=mData.push().getKey();
        val mAuth_past: FirebaseAuth = FirebaseAuth.getInstance()
        mData_past.addListenerForSingleValueEvent(object : ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
                for (orderDS in snapshot.getChildren()) {
                    if (mAuth_past.currentUser?.uid.equals(orderDS.child("User").getValue(String::class.java))) {
                        if (mAuth.getCurrentUser()?.getUid()
                            .equals(orderDS.child("User").getValue(String::class.java))
                    ) {
                        orderDS.getKey()?.let {
                            mData_past.child(it).child("order")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (vegetableDS in snapshot.getChildren()) {
                                            //Toast.makeText(OrderPlaced.this, vegetableDS.getKey(), Toast.LENGTH_SHORT).show();
                                            if (veggies_past != null) {
                                                vegetable_past = vegetableDS.child("Name")
                                                    .getValue(String::class.java)
                                                    .toString() + ": " + vegetableDS.child("Value")
                                                    .getValue(
                                                        String::class.java
                                                    )
                                                veg_past!!.add(vegetable_past)
                                            } else {
                                                vegetable_past = vegetableDS.child("Name")
                                                    .getValue(String::class.java)
                                                    .toString() + ": " + vegetableDS.child("Value")
                                                    .getValue(
                                                        String::class.java
                                                    )
                                                veg_past!!.add(vegetable_past)
                                            }
                                        }
                                        if (veg_past != null) {
                                            veggies_past = ArrayOrderPassData(veg_past, "PRICE")
                                            vegetablesList_past!!.add(veggies_past!!)
                                        }
                                        vegAdapter_past!!.notifyDataSetChanged()
                                        //veggies="";
                                        veg!!.clear()
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        }
                    }
                }
                }
            }

           override fun onCancelled(error: DatabaseError) {}
        })
    }
}