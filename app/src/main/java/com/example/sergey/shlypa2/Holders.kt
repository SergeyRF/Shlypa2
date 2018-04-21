package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.sergey.shlypa2.R.id.tvScores
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.TeamWithScores
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
    val teamName : TextView = view.findViewById(R.id.tvTeamName)
    val playersList : LinearLayout = view.findViewById(R.id.llPlayers)
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
        teemScores.text = scoredTeam.scoresMap.values.sum().toString()

        listPlayers.removeAllViews()

        for (player in scoredTeam.team.players) {

            val playerView = LayoutInflater.from(view.context)
                    .inflate(R.layout.holder_player_score, listPlayers, false)

            val playerHolder = PlayerWithScoreHolder(playerView)
            playerHolder.bind(player, scoredTeam.scoresMap[player.id] ?: 0)

            listPlayers.addView(playerView)
        }
    }
}

class PlayerHolder(val view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.wordInject)
    val etName: EditText = view.findViewById(R.id.etRename)
    val avatarImage: CircleImageView = view.findViewById(R.id.civPlayerAvatar)

    fun bind(player: Player) {
        tvName.show()
        etName.hide()
        tvName.text = player.name

        itemView.setOnClickListener {
            tvName.hide()
            etName.show()
            etName.setText("")
            etName.append(player.name)
            etName.requestFocus()
            Functions.showKeyboard(view.context, etName)
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

            }
        }

        Picasso.get()
                .load(Functions.imageNameToUrl(player.avatar))
                .into(avatarImage)
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

class PlayerInTeamHolder(view : View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvName)
    val avatarImage : CircleImageView = view.findViewById(R.id.civPlayerAvatar)

    fun bind(player: Player) {
        tvName.text = player.name
        Picasso.get()
                .load(Functions.imageNameToUrl(player.avatar))
                .into(avatarImage)
    }
}

class WordsHolder(val view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.wordInject)
    val ibRenameWodr: ImageButton = view.findViewById(R.id.ibRenameWord)
    val ibDeletWord: ImageButton = view.findViewById(R.id.ibDelWord)
    val ibNextWord: ImageButton = view.findViewById(R.id.ibNextWord)

    fun bind(word: Word) {

        tvName.text = word.word
        Timber.d("${word.word}")

        buttonLook(word)

        ibRenameWodr.setOnClickListener {
            listener?.invoke(word)
        }

        ibDeletWord.setOnClickListener {
            listenerTwo?.invoke(word)

        }
        ibNextWord.setOnClickListener {
            listenerThree?.invoke(word)
            Timber.d("NextWord")
        }


    }

    fun buttonLook(word: Word) {
        if (word.type == Contract.WordType.USER) {
            ibNextWord.hide()
            ibRenameWodr.show()
        } else {
            ibRenameWodr.hide()
            ibNextWord.show()
        }

    }
}

class WordResultHolder(view: View) : BaseHolder(view) {
    val wordRv: TextView = view.findViewById(R.id.word_out)
    val radioGroup: RadioGroup = view.findViewById(R.id.groupCorrect)

    fun bind(word: Word) {
        wordRv.text = word.word
        radioGroup.check(if (word.right) R.id.radioCorrect else R.id.radioWrong)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            word.right = checkedId == R.id.right
        }
    }
}

class SavedStateHolder(val view: View) : BaseHolder(view) {
    val tvDate: TextView = view.findViewById(R.id.tvDateState)
    val tvPlayers: TextView = view.findViewById(R.id.tvPlayersState)

    fun bind(state: GameState) {
        tvDate.text = Functions.timeToLocalDate(state.savedTime, view.context)
        val builder = StringBuilder()
        state.players.forEach {
            builder.append("${it.value.name},")
        }

        tvPlayers.text = builder.toString()

        view.setOnClickListener {
            listener?.invoke(state)
        }
    }
}