package com.example.sergey.shlypa2.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.dimen
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

object PlayerInTeamView {
    fun inflatePlayers(players: MutableList<Player>, context: Context): LinearLayout {
        val root = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
        }

        val margin = root.dimen(R.dimen.in_card_horizontal_margin)
        val avatarSize = root.dimen(R.dimen.player_in_team_avatar)
        val textSize = root.dimen(R.dimen.text_smoll).toFloat()
        val textColor = ContextCompat.getColor(context, R.color.primary_text)

        for (player in players) {
            val child = LinearLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                orientation = LinearLayout.HORIZONTAL
            }

            val avatarView = CircleImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(avatarSize,
                        avatarSize).apply {
                    leftMargin = margin
                    rightMargin = margin
                }
            }

            val tvName = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT).apply {
                    weight = 1F
                }
                text = player.name
                this.textSize = textSize
                setTextColor(textColor)
            }

            child.addView(avatarView)
            child.addView(tvName)
            Picasso.get()
                    .load(Functions.imageNameToUrl("player_avatars/small/${player.avatar}"))
                    .into(avatarView)

            root.addView(child)
        }

        return root
    }
}