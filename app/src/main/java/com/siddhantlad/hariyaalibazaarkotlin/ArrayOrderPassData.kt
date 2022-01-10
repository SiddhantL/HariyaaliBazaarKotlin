package com.siddhantlad.hariyaalibazaarkotlin

import java.util.ArrayList

class ArrayOrderPassData(listName: ArrayList<String>, price: String) {
    val listName = ArrayList<String>()
    var price: String

    init {
        if (listName != null) this.listName.addAll(listName)
        this.price = price
    }
}