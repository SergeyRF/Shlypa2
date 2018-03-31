package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by sergey on 3/29/18.
 */
class PlayerAdapter():RecyclerView.Adapter<Holder>(){
    override fun onBindViewHolder(holder: Holder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Holder(val view:View):RecyclerView.ViewHolder(view){
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(){
        tvName.text = "fgfdg"
    }
}