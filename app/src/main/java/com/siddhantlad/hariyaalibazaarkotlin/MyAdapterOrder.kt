package com.siddhantlad.hariyaalibazaarkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlin.collections.ArrayList

class MyAdapterOrder     // Counstructor for the Class
    (// List to store all the contact details
    private val functionCallTest:(orderAdd:Vegetables)->Unit,
    private var contactsList: ArrayList<Vegetables?>,
    private val mContext: Context
) : RecyclerView.Adapter<MyAdapterOrder.ContactHolder?>() {
    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.getContext())

        // Inflate the layout view you have created for the list rows here
        val view: View = layoutInflater.inflate(R.layout.veg_item, parent, false)
        return ContactHolder(view)
    }

    // This method is called when binding the data to the views being created in RecyclerView
    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val contact = contactsList!![position]

        if (contact != null) {
        // Set the data to the views here
        holder.setVegetableTitle(contact.name)
        holder.setVegetableDesc(contact.number)
        val quant: TextView = holder.itemView.findViewById<TextView>(R.id.itemQuanEt)
        Glide.with(mContext.applicationContext).load(contact.imageStr)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.hb)
            .into(holder.logo)
            quant.setText(contact.orderStock.toString() + " " + contact.unit)
            holder.priceTV.setText(contact.pricepkg.toString() + " ₹ " + contact.unit)
            holder.itemView.findViewById<View>(R.id.removeBtn)
                .setOnClickListener(View.OnClickListener {
                    val quant: TextView = holder.itemView.findViewById<TextView>(R.id.itemQuanEt)
                    var quantityStr = String()
                    var fullText = String()
                    fullText = quant.getText().toString()
                    quantityStr = fullText.substring(0, fullText.indexOf(" "))
                    if (quantityStr.toDouble() != 0.0) {
                        var quantDouble = quantityStr.toDouble()
                        quantDouble = quantDouble - 0.25
                        quant.setText(quantDouble.toString() + " " + contact.unit)
                        val contactOrder = Vegetables(
                            contact.id,
                            contact.name,
                            contact.number,
                            contact.orderStock,
                            contact.stock,
                            contact.unit,
                            contact.imageStr,
                            contact.pricepkg
                        )
                        contact.orderStock = quantDouble
                        contactOrder.orderStock = quantDouble
                        functionCallTest(contactOrder)
                    }
                })
            holder.itemView.findViewById<View>(R.id.addBtn)
                .setOnClickListener(View.OnClickListener {
                    val quant: TextView = holder.itemView.findViewById<TextView>(R.id.itemQuanEt)
                    var quantityStr = String()
                    var fullText = String()
                    fullText = quant.getText().toString()
                    quantityStr = fullText.substring(0, fullText.indexOf(" "))
                    if (quantityStr.toDouble() != contact.stock) {
                        var quantDouble = quantityStr.toDouble()
                        quantDouble = quantDouble + 0.25
                        quant.setText(quantDouble.toString() + " " + contact.unit)
                        val contactOrder = Vegetables(
                            contact.id,
                            contact.name,
                            contact.number,
                            contact.orderStock,
                            contact.stock,
                            contact.unit,
                            contact.imageStr,
                            contact.pricepkg
                        )
                        contact.orderStock = quantDouble
                        contactOrder.orderStock = quantDouble
                        functionCallTest(contactOrder)
                    }
                })
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            //onImageClickListener.onImageClick(contact);
        })
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public
    }


    // This is your ViewHolder class that helps to populate data to the view
    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView
        private val txtDescription: TextView
        val logo: ImageView
        val priceTV: TextView
        fun setVegetableTitle(name: String?) {
            txtTitle.setText(name)
        }

        fun setVegetableDesc(number: String?) {
            txtDescription.setText(number)
        }

        init {
            logo = itemView.findViewById(R.id.imageView)
            txtTitle = itemView.findViewById<TextView>(R.id.text_view_title)
            txtDescription = itemView.findViewById<TextView>(R.id.text_view_description)
            priceTV = itemView.findViewById<TextView>(R.id.priceTV)
        }
    }

    interface OnImageClickListener {
        fun onImageClick(orderAdd: Vegetables?)
    }

    override fun getItemCount(): Int {
        if (contactsList!=null) {
            return contactsList!!.size
        }else{
            return 0
        }
    }
}