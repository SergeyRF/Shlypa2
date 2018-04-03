package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by sergey on 4/3/18.
 */
class WordAdapter(): RecyclerView.Adapter<HolderW>(){

    var wordS : List<Word>?= null

    override fun onBindViewHolder(holder: HolderW, position: Int) {
        val player : Word = wordS!![position]
        holder.bind(player.word)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HolderW {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.holder_player,parent,false)
        return HolderW(view)
    }

    override fun getItemCount(): Int {
        return if(wordS == null) 0 else wordS!!.size
    }

    fun setWords(data : List<Word>?){
        wordS = data
        notifyDataSetChanged()}

}

class HolderW(view: View): RecyclerView.ViewHolder(view){
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(string:String){
        tvName.text = string
    }
}