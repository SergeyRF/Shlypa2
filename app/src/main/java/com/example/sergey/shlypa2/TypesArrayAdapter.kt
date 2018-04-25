package com.example.sergey.shlypa2

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sergey.shlypa2.game.WordType

/**
 * Created by alex on 4/25/18.
 */

class TypesArrayAdapter(context : Context, layout : Int, types : Array<WordType> )
    : ArrayAdapter<WordType>(context, layout, types) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getView(position, convertView, parent)
        val tv : TextView = view.findViewById(android.R.id.text1)
        tv.text = context.getString(getItem(position).title)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv : TextView = view.findViewById(android.R.id.text1)
        tv.text = context.getString(getItem(position).title)

        return view
    }
}