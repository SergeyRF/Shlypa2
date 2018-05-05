package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.game.WordType
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.utils.show
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber


/**
 * Created by alex on 4/4/18.
 */

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
    var listener: ((Any) -> Unit)? = null
    var listenerTwo: ((Any) -> Unit)? = null
    var listenerThree: ((Any) -> Unit)? = null
}

class TeamHolder(val view: View) : BaseHolder(view) {
    val teamName: TextView = view.findViewById(R.id.tvTeamName)
    val playersList: LinearLayout = view.findViewById(R.id.llPlayers)
    val ivRename = view.findViewById<ImageButton>(R.id.ibTeemRename)

    fun bind(team: Team) {
        teamName.text = team.name

        playersList.removeAllViews()
        for (player in team.players) {
            Timber.d("add player into listview")
            val playerView = LayoutInflater.from(view.context)
                    .inflate(R.layout.holder_player_inteam, playersList, false)

            val holder = PlayerInTeamHolder(playerView)
            holder.bind(player)

            playersList.addView(playerView)
        }


        ivRename.setOnClickListener {
            listener?.invoke(team)
        }

        teamName.setOnLongClickListener {
            listener?.invoke(team)
            true
        }
    }
}

class TeamWithScoreHolder(val view: View) : BaseHolder(view) {
    private val teamName: TextView = view.findViewById(R.id.tvTeamName)
    private val teemScores: TextView = view.findViewById(R.id.tvTeamScores)
    private val listPlayers: LinearLayout = view.findViewById(R.id.linearPlayers)

    fun bind(scoredTeam: TeamWithScores) {
        teamName.text = scoredTeam.team.name
        teemScores.text = scoredTeam.getScores().toString()

        listPlayers.removeAllViews()

        for (player in scoredTeam.team.players) {

            val playerView = LayoutInflater.from(view.context)
                    .inflate(R.layout.holder_player_inteam, listPlayers, false)

            val playerHolder = PlayerInTeamHolder(playerView)
            playerHolder.bind(player, scoredTeam.scoresMap[player.id] ?: 0)

            listPlayers.addView(playerView)
        }
    }
}

class PlayerHolder(val view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.wordInject)
    val etName: EditText = view.findViewById(R.id.etRename)
    val avatarImage: CircleImageView = view.findViewById(R.id.civPlayerAvatar)
    val delPlayer: ImageButton = view.findViewById(R.id.ib_delPlayer)

    fun bind(player: Player) {
        tvName.show()
        etName.hide()
        delPlayer.hide()
        tvName.text = player.name

        itemView.setOnClickListener {
            tvName.hide()
            etName.show()
            delPlayer.show()
            etName.setText("")
            etName.append(player.name)
            etName.requestFocus()
            Functions.showKeyboard(view.context, etName)
        }

        delPlayer.setOnClickListener {
            listenerTwo?.invoke(player)
        }

        etName.setOnFocusChangeListener { view, hasFocus ->
            Timber.d(" focus changed$hasFocus")
            if (!hasFocus) {
                if (etName.text.isNotEmpty() && etName.text.toString() != player.name) {
                    player.name = etName.text.toString()
                    tvName.text = player.name

                    listener?.invoke(player)
                }

                tvName.show()
                etName.hide()
                delPlayer.hide()
            }
        }

        Picasso.get()
                .load(Functions.imageNameToUrl("player_avatars/small/${player.avatar}"))
                .into(avatarImage)
    }
}

class PlayerInTeamHolder(view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvScores: TextView = view.findViewById(R.id.tvScores)
    val avatarImage: CircleImageView = view.findViewById(R.id.civPlayerAvatar)

    fun bind(player: Player, scores: Int = Int.MIN_VALUE) {
        tvName.text = player.name
        if (scores != Int.MIN_VALUE) {
            tvScores.show()
            tvScores.text = scores.toString()
        } else {
            tvScores.hide()
        }

        Picasso.get()
                .load(Functions.imageNameToUrl("player_avatars/small/${player.avatar}"))
                .into(avatarImage)
    }
}

class WordsHolder(val view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.wordInject)
    val ibDeletWord: ImageButton = view.findViewById(R.id.ibDelWord)
    val ibNextWord: ImageButton = view.findViewById(R.id.ibNextWord)
    val etReNameW: TextView = view.findViewById(R.id.etWordRename)

    fun bind(word: Word) {
        etReNameW.text = word.word
        etReNameW.hide()
        tvName.show()
        if (word.type == WordType.USER) {
            ibNextWord.hide()
        }

        tvName.text = word.word
        Timber.d("${word.word}")



        ibDeletWord.setOnClickListener {
            listenerTwo?.invoke(word)

        }
        ibNextWord.setOnClickListener {
            listenerThree?.invoke(word)
            Timber.d("NextWord")
        }

        tvName.setOnClickListener {
            tvName.hide()
            etReNameW.show()
            etReNameW.setText("")
            etReNameW.append(word.word)
            etReNameW.requestFocus()
            Functions.showKeyboard(view.context, etReNameW)
        }

        etReNameW.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (etReNameW.text.isNotEmpty() && etReNameW.text.toString() != word.word) {
                    word.word = etReNameW.text.toString()
                    tvName.text = word.word
                    ibNextWord.hide()
                    listener?.invoke(word)
                }

                tvName.show()
                etReNameW.hide()
            }
        }


    }

}

class WordResultHolder(view: View) : BaseHolder(view) {
    val wordRv: TextView = view.findViewById(R.id.word_out)
    val radioGroup: RadioGroup = view.findViewById(R.id.groupCorrect)

    fun bind(word: Word) {
        wordRv.text = word.word
        //avoid bug
        radioGroup.setOnCheckedChangeListener(null)

        radioGroup.check(if (word.right) R.id.radioCorrect else R.id.radioWrong)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            Timber.d("radio button checked")
            word.right = checkedId == R.id.right
        }
    }
}

class SavedStateHolder(val view: View) : BaseHolder(view) {
    val tvDate: TextView = view.findViewById(R.id.tvDateState)
    val tvPlayers: TextView = view.findViewById(R.id.tvPlayersState)

    fun bind(state: GameState) {
        val dateText = Functions.timeToLocalDateWithTime(state.savedTime, view.context)
        tvDate.text = dateText
        val builder = StringBuilder()
        state.players.forEach {
            builder.append("${it.value.name} \n")
        }

        tvPlayers.text = builder.toString()

        view.setOnClickListener {
            listener?.invoke(state)
        }
    }
}