package com.siddhantlad.hariyaalibazaarkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.util.toRange
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import com.google.firebase.database.*

class OrderPage : AppCompatActivity() {
    lateinit var vegAdapter: MyAdapterOrder
    lateinit var vegetablesList: ArrayList<Vegetables?>
    var price = 0.0
    lateinit var priceTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_page)
        val window: Window = getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)
        priceTV = findViewById<TextView>(R.id.totalcost)
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val appbarTV: TextView = toolbar.findViewById<View>(R.id.appbartextview) as TextView
        appbarTV.setText(R.string.app_name)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        val carListAsString: String? = getIntent().getStringExtra("list_as_string")
        val gson = Gson()
        val type: Type = object : TypeToken<List<Vegetables?>?>() {}.getType()
        val vegListOrder: List<Vegetables> = gson.fromJson(carListAsString, type)
        price = 0.0
        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.recycler)
        vegetablesList = ArrayList()
        /*val obj = object : MyAdapter.OnImageClickListener {
            override fun OnImageClickListener() {
                TODO("Not yet implemented")
            }

            override fun onImageClick(orderAdd: Vegetables?) {
                for (i in vegetablesList!!.indices) {
                    if (vegetablesList!![i]!!.id == orderAdd!!.id) {
                        vegetablesList!!.removeAt(i)
                    }
                }
                vegetablesList!!.add(orderAdd)
                if (orderAdd!!.orderStock == 0.0) {
                    vegetablesList!!.remove(orderAdd)
                }
                price = 0.0
                for (i in vegetablesList!!.indices) {
                    price = price + vegetablesList!![i]!!.pricepkg * vegetablesList!![i]!!.orderStock
                }
                priceTV.setText(
                    "Order Total: " + java.lang.Double.toString(
                        price.toInt().toDouble()
                    ) + " ₹"
                )
                if (vegetablesList!!.size > 0) {
                    findViewById<View>(R.id.buttonConfirmTV).setBackgroundColor(getResources().getColor(R.color.darkgreen))
                } else {
                    findViewById<View>(R.id.buttonConfirmTV).setBackgroundColor(getResources().getColor(R.color.cardview_dark_background))
                }
                //Toast.makeText(this, orderAdd.getName()+" Added", Toast.LENGTH_SHORT).show();
                // handle image data
            }

        }*/
        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        vegAdapter = MyAdapterOrder(::onImageClick,
            vegetablesList,
            this)
             recyclerView.setAdapter(vegAdapter)
        if (vegListOrder != null) {
            for (i in vegListOrder.indices) {
                vegetablesList!!.add(vegListOrder[i])
                vegAdapter.notifyDataSetChanged()
                price = price + vegListOrder[i].pricepkg * vegetablesList!![i]!!.orderStock
                val priceTv: TextView = findViewById<TextView>(R.id.totalcost)
                priceTv.setText(
                    "Order Total: " + java.lang.Double.toString(
                        price.toInt().toDouble()
                    ) + " ₹"
                )
            }
            if (vegListOrder.size > 0) {
                val btnConfirm: TextView = findViewById<TextView>(R.id.buttonConfirmTV)
                btnConfirm.setBackgroundColor(getResources().getColor(R.color.darkgreen))
            }
        }
        findViewById<View>(R.id.buttonConfirmTV).setOnClickListener(View.OnClickListener {
            //if (vegetablesList!!.size > 0) {
              if(price>0.0){
                val mData: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("currOrders")
                val pushStr: String? = mData.push().getKey()
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                if (mAuth.getCurrentUser() != null) {
                    if (pushStr != null) {
                        mData.child(pushStr).child("User").setValue(mAuth.getCurrentUser()!!.getUid())
                    for (i in vegetablesList!!.indices) {
                        mData.child(pushStr).child("Price").setValue(price.toString())
                        mData.child(pushStr).child("order").child(vegetablesList!![i]!!.id)
                            .child("Value").setValue(
                            vegetablesList!![i]!!
                                .orderStock.toString() + " " + vegetablesList!![i]!!.unit
                        )
                        mData.child(pushStr).child("order").child(vegetablesList!![i]!!.id)
                            .child("Name").setValue(
                            vegetablesList!![i]!!.name
                        )
                    }
                    val intent = Intent(this@OrderPage, OrderPlaced::class.java)
                    startActivity(intent)
                }
                }
            }
        })
    }

    fun onImageClick(orderAdd: Vegetables?) {
        for (item in vegetablesList) {
            if (item!!.id.equals(orderAdd!!.id)) {
                Toast.makeText(this,item!!.name,Toast.LENGTH_SHORT).show()
                //vegetablesList.remove(orderAdd)
                item.orderStock=orderAdd.orderStock
            }
        }
        //vegetablesList.add(orderAdd)

        if (orderAdd!!.orderStock == 0.0) {
            vegetablesList.remove(orderAdd)
        }
        price = 0.0
        for (items in vegetablesList) {
            price = price + items!!.pricepkg * items!!.orderStock
        }
        priceTV.text = "Order Total: " + java.lang.Double.toString(price.toInt().toDouble()) + " ₹"

        if (price > 0.0) {
            findViewById<View>(R.id.buttonConfirmTV).setBackgroundColor(resources.getColor(R.color.darkgreen))
        } else {
            findViewById<View>(R.id.buttonConfirmTV).setBackgroundColor(resources.getColor(R.color.cardview_dark_background))
        }
    }


}