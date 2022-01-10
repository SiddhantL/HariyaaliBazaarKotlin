package com.siddhantlad.hariyaalibazaarkotlin

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.siddhantlad.hariyaalibazaarkotlin.AdapterOrders.ContactHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.siddhantlad.hariyaalibazaarkotlin.R
import android.widget.TextView
import java.util.ArrayList
//FIX
class AdapterOrders(contactsList: ArrayList<ArrayOrderPassData>?, context: Context) :
    RecyclerView.Adapter<ContactHolder>() {
    // List to store all the contact details
    var contactsList: ArrayList<ArrayOrderPassData>?=contactsList
    var mContext: Context=context

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Inflate the layout view you have created for the list rows here
        val view: View = layoutInflater.inflate(R.layout.order_item, parent, false)
        return ContactHolder(view)
    }

    override fun getItemCount(): Int {
        return contactsList?.size ?: 0
    }

    // This method is called when binding the data to the views being created in RecyclerView
    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val contact: ArrayOrderPassData = contactsList!![position]
        // Set the data to the views here
        holder.setVegetableTitle("")
        for (i in 0 until contact.listName.size) {
            if (i == 0) {
                holder.setVegetableTitle(contact.listName.get(i))
            } else {
                holder.setVegetableTitle(
                    """
    ${holder.txtTitle.text}
    ${contact.listName.get(i)}
    """.trimIndent()
                )
            }
        }
        val quant = holder.itemView.findViewById<TextView>(R.id.itemQuanEt)
        quant.setText(contact.price)
    }

    // This is your ViewHolder class that helps to populate data to the view
    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView
        fun setVegetableTitle(name: String?) {
            txtTitle.text = name
        }

        init {
            txtTitle = itemView.findViewById(R.id.text_view_title)
        }
    }

}