package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.Team
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.utils.show
import timber.log.Timber


/**
 * Created by alex on 4/4/18.
 */

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
    var listener : ((Any) -> Unit)? = null
}

class TeamHolder(view: View) : BaseHolder(view) {
    val teamName = view.findViewById<TextView>(R.id.team_name)
    val listPlayers = view.findViewById<TextView>(R.id.tvPlayers)

    fun bind(teem: Team) {
        teamName.text = teem.name

        var player = StringBuffer("")
        for (i in 0 until teem.players.size) {
            player.append(teem.players[i].name + "\n")
        }
        listPlayers.text = player

    }
}

class TeamWithScoreHolder(val view: View) : BaseHolder(view) {
    private val teamName : TextView = view.findViewById(R.id.team_name)
    private val teemScores : TextView = view.findViewById(R.id.tvTeamScores)
    private val listPlayers : LinearLayout = view.findViewById(R.id.linearPlayers)

    fun bind(scoredTeam: TeamWithScores) {
        teamName.text = scoredTeam.team.name
        teemScores.text = scoredTeam.scoresMap.values.sum().toString()

        listPlayers.removeAllViews()

        for(player in scoredTeam.team.players) {

            val playerView = LayoutInflater.from(view.context)
                    .inflate(R.layout.holder_player_score, listPlayers, false)

            val playerHolder = PlayerWithScoreHolder(playerView)
            playerHolder.bind(player, scoredTeam.scoresMap[player.id] ?: 0)

            listPlayers.addView(playerView)
        }
    }
}

class PlayerHolder(view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.wordInject)
    val etName: EditText = view.findViewById(R.id.etRename)
    val btOnRename: Button = view.findViewById(R.id.btOnRename)
    fun bind(player: Player) {
        etName.hide()
        btOnRename.hide()
        tvName.text = player.name
        itemView.setOnClickListener{
            tvName.hide()
            etName.show()
            btOnRename.show()
            etName.setText(player.name)
        }
        btOnRename.setOnClickListener{
           if (etName.text.isNotEmpty()) {
               player.name = etName.text.toString()
           }
            tvName.show()
            tvName.text = player.name
            etName.hide()
            btOnRename.hide()
            listener?.invoke(player)
        }

    }
}

class PlayerWithScoreHolder(view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvScores: TextView = view.findViewById(R.id.tvScores)

    fun bind(player: Player, scores: Int) {
        tvName.text = player.name
        tvScores.text = scores.toString()
    }
}

class WordsHolder(view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.wordInject)

    fun bind(word: Word) {
        tvName.text = word.word
        Timber.d("${word.word}")
    }
    /*val tvName: TextView = view.findViewById(R.id.wordInject)
    val etName: EditText = view.findViewById(R.id.etRename)
    val btOnRename: Button = view.findViewById(R.id.btOnRename)
    fun bind(word:Word) {
        etName.hide()
        btOnRename.hide()
        tvName.text = word.word
        itemView.setOnClickListener{
            tvName.hide()
            etName.show()
            btOnRename.show()
            etName.setText(word.word)
        }
        btOnRename.setOnClickListener{
            if (etName.text.isNotEmpty()) {
                word.word = etName.text.toString()
            }
            tvName.show()
            tvName.text = word.word
            etName.hide()
            btOnRename.hide()
            listener?.invoke(word)
        }
        }*/
}

class WordResultHolder(view: View):BaseHolder(view) {
    val wordRv: TextView = view.findViewById(R.id.word_out)
    val radioGroup : RadioGroup = view.findViewById(R.id.groupCorrect)

    fun bind(word: Word) {
        wordRv.text = word.word
        radioGroup.check(if(word.right) R.id.radioCorrect else R.id.radioWrong)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            word.right = checkedId == R.id.right
        }
    }
}