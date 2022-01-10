package com.siddhantlad.hariyaalibazaarkotlin

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.firebase.database.*
import com.siddhantlad.hariyaalibazaarkotlin.submissioncriteria.NoteVegetableActivity
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var vegAdapter: MyAdapter
    lateinit var vegetablesList: ArrayList<Vegetables>
    var order: MutableList<Vegetables> = ArrayList<Vegetables>()
    lateinit var confirm: TextView
    lateinit var queryTextListener: SearchView.OnQueryTextListener
    var counter = 0
    lateinit var priceTV: TextView
    var price = 0.0
    fun functionCallTest(id:Int){
Toast.makeText(this,"HMMM",Toast.LENGTH_LONG).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val appbarTV = toolbar.findViewById<View>(R.id.appbartextview) as TextView
        appbarTV.setText(R.string.app_name)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)
        priceTV = findViewById(R.id.totalcost)
        confirm = findViewById<View>(R.id.buttonConfirmTV) as TextView
        confirm!!.setOnClickListener {
            if (order.size > 0) {
                val gson = Gson()
                val jsonOrder: String = gson.toJson(order)
                val intent = Intent(this@MainActivity, OrderPage::class.java)
                intent.putExtra("list_as_string", jsonOrder)
                startActivity(intent)
            }
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        vegetablesList = ArrayList<Vegetables>()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        vegAdapter = MyAdapter(::onImageClick,vegetablesList!!, this)

        recyclerView.adapter = vegAdapter
        counter = 0
        val mData: DatabaseReference = FirebaseDatabase.getInstance().getReference("vegetables")
        mData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.getChildren()) {
                    vegetablesList!!.add(
                        Vegetables(
                            ds.getKey().toString(),
                            ds.child("Name").getValue() as String,
                            ds.child("HindiName").getValue() as String,
                            0.0,
                            ds.child("Stockikg").getValue() as Double,
                            ds.child("unit").getValue() as String,
                            ds.child("ImgAddrs").getValue() as String,
                            ds.child("Pricepkg").getValue() as Long
                        )
                    )
                    vegAdapter!!.notifyDataSetChanged()
                    counter++
                }
            }

           override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.main, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_action_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )
        if (searchView != null) {
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    Log.i("onQueryTextChange", newText)
                    filter(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.i("onQueryTextSubmit", query)
                    return true
                }
            }
            searchView.setOnQueryTextListener(queryTextListener)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_orders -> {
                startActivity(Intent(this@MainActivity, OrderPlaced::class.java))
            }
            R.id.menu_action_notes -> {
                startActivity(Intent(this@MainActivity, NoteVegetableActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Vegetables> = ArrayList<Vegetables>()

        // running a for loop to compare elements.
        for (item in vegetablesList!!) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name.toLowerCase().contains(text.toLowerCase()) || item.number
                    .toLowerCase().contains(text.toLowerCase())
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            vegAdapter?.filterList(filteredlist)
        }
    }


   fun onImageClick(orderAdd: Vegetables?) {
        for (i in order.indices) {
            if (orderAdd != null) {
                if (order[i].id.equals(orderAdd.id)) {
                    order.removeAt(i)
                    break
                }
            }
        }
        if (orderAdd != null) {
            order.add(orderAdd)
        }
        if (orderAdd != null) {
            if (orderAdd.orderStock === 0.0) {
                order.remove(orderAdd)
            }
        }
        price = 0.0
        for (i in order.indices) {
            price = price + order[i].pricepkg * order[i].orderStock
        }
        priceTV!!.text =
            "Order Total: " + java.lang.Double.toString(price.toInt().toDouble()) + " ₹"
        if (order.size > 0) {
            findViewById<View>(R.id.buttonConfirmTV).setBackgroundColor(resources.getColor(R.color.darkgreen))
        } else {
            findViewById<View>(R.id.buttonConfirmTV).setBackgroundColor(resources.getColor(R.color.cardview_dark_background))
        }
        //Toast.makeText(this, orderAdd.getName()+" Added", Toast.LENGTH_SHORT).show();
        // handle image data
    }


}
