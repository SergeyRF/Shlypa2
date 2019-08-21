package com.example.sergey.shlypa2

import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.getSmallImage
import com.example.sergey.shlypa2.extensions.gone
import com.example.sergey.shlypa2.extensions.hide
import com.example.sergey.shlypa2.extensions.show
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.views.HolderInflater
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber


/**
 * Created by alex on 4/4/18.
 */

abstract class BaseHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
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
        playersList.addView(HolderInflater.inflatePlayers(team.players, view.context))
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
        listPlayers.addView(HolderInflater.inflatePlayers(scoredTeam.team.players,
                view.context,
                scoredTeam.scoresMap))
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

        Glide.with(itemView)
                .load(player.getSmallImage(view.context))
                .into(avatarImage)
    }
}

class WordsHolder(val view: View, private val flagChange: Boolean) : BaseHolder(view) {
    private val tvName: TextView = view.findViewById(R.id.wordInject)
    private val ibDeleteWord: ImageButton = view.findViewById(R.id.ibDelWord)
    private val ibChangeWord: ImageButton = view.findViewById(R.id.ibChangeWord)

    fun bind(word: Word) {
        tvName.text = word.word
        if (!flagChange) ibChangeWord.gone()

        ibDeleteWord.setOnClickListener {
            listenerTwo?.invoke(word)
        }

        ibChangeWord.setOnClickListener {
            listenerThree?.invoke(word)
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