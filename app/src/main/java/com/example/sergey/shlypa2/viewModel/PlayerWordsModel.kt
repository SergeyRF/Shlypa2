package com.example.sergey.shlypa2.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.provider.UserDictionary
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Player
import com.example.sergey.shlypa2.game.Word

/**
 * Created by sergey on 4/3/18.
 */
class PlayerWordsModel():ViewModel(){
    private val hatWords = MutableLiveData<List<Word>>()
    private val playerLivaData = MutableLiveData<Player>()
    private var pos:Int=0
    private var words:MutableList<Word> = mutableListOf()
    private var help:Boolean = false
    init {
        updatePVM()
    }
    fun addWordGameLD() {
        for(word in words) {
            Game.addWord(word)
        }
        words.clear()
        pos++
        if (pos==Game.getPlayers().size){
            help = true
            pos--
        }
        updatePVM()
    }
    fun GoGame() = help
    fun needWord():Boolean{
        return words.size != Game.words
    }
    fun getPlayer():LiveData<Player> {
       return playerLivaData
    }
    fun addWordLD(s:String){
        words.add(Word(s))
        updatePVM()
    }

    fun getWordPlayer(): LiveData<List<Word>> = hatWords

    fun updatePVM(){
        hatWords.value = words
        playerLivaData.value = Game.getPlayers()[pos]
    }
}