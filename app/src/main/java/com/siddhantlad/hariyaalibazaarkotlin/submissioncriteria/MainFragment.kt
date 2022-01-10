package com.siddhantlad.hariyaalibazaarkotlin.submissioncriteria
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.siddhantlad.hariyaalibazaarkotlin.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {
    val ADD_NOTE_REQUEST = 1
    val EDIT_NOTE_REQUEST = 2
    lateinit var navController: NavController
    lateinit var vm: NoteViewModel
    lateinit var adapter:NoteAdapter
    lateinit var recycler_view:RecyclerView
    lateinit var title:String
    lateinit var description:String
    var priority:Int=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = ViewModelProviders.of(this)[NoteViewModel::class.java]

        vm.getAllNotes().observe(viewLifecycleOwner, Observer {
            Log.i("Notes observed", "$it")

            adapter.submitList(it)
        })
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    private fun setUpListeners() {
        button_add_note.setOnClickListener {
            val intent = Intent(activity, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        // swipe listener
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.getNoteAt(viewHolder.adapterPosition)
                vm.delete(note)
            }

        }).attachToRecyclerView(recycler_view)
    }

    private fun setUpRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)

        adapter = NoteAdapter { clickedNote ->
            val intent = Intent(activity, AddEditNoteActivity::class.java)
            intent.putExtra(EXTRA_ID, clickedNote.id)
            intent.putExtra(EXTRA_TITLE, clickedNote.title)
            intent.putExtra(EXTRA_DESCRIPTION, clickedNote.description)
            intent.putExtra(EXTRA_PRIORITY, clickedNote.priority)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }
        recycler_view.adapter = adapter
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null && requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            title = data.getStringExtra(EXTRA_TITLE).toString()
            description= data.getStringExtra(EXTRA_DESCRIPTION).toString()
            priority = data.getIntExtra(EXTRA_PRIORITY, -1)
            vm.insert(Note(title, description, priority))
            Toast.makeText(activity, "Note inserted!", Toast.LENGTH_SHORT).show()

        } else if(data != null && requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data.getIntExtra(EXTRA_ID, -1)
            if(id == -1) {
                Toast.makeText(activity, "Item couldn't be updated!", Toast.LENGTH_SHORT).show()
                return
            }
            title = data.getStringExtra(EXTRA_TITLE).toString()
            description= data.getStringExtra(EXTRA_DESCRIPTION).toString()
            priority = data.getIntExtra(EXTRA_PRIORITY, -1)
            vm.update(Note(title, description, priority, id))
            Toast.makeText(activity, "Item updated!", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(activity, "Item not saved!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recycler_view=view.findViewById(R.id.recycler_view)

        setUpRecyclerView()

        setUpListeners()

        view.findViewById<FloatingActionButton>(R.id.buttoninfo).setOnClickListener(this)
        view.findViewById<Button>(R.id.send_money_btn).setOnClickListener(this)
        view.findViewById<Button>(R.id.view_balance_btn).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttoninfo -> navController!!.navigate(R.id.action_mainFragment_to_viewTransactionFragment)
            R.id.send_money_btn -> navController!!.navigate(R.id.action_mainFragment_to_chooseRecipientFragment)
            R.id.view_balance_btn -> navController!!.navigate(R.id.action_mainFragment_to_viewBalanceFragment)
        }
    }



}
