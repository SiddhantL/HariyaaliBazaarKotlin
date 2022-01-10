package com.siddhantlad.hariyaalibazaarkotlin.submissioncriteria

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.siddhantlad.hariyaalibazaarkotlin.R


class NoteVegetableActivity : AppCompatActivity() {

    private lateinit var vm: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_vegetable_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val appbarTV = toolbar.findViewById<View>(R.id.appbartextview) as TextView
        appbarTV.setText(R.string.app_name)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkgreen)

        //setUpRecyclerView()
        //setUpListeners()
        vm = ViewModelProviders.of(this)[NoteViewModel::class.java]
        vm.getAllNotes().observe(this, Observer {
            Log.i("Notes observed", "$it")
            //adapter.submitList(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                vm.deleteAllNotes()
                Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

