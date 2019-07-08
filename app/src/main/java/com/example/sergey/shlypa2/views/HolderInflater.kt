package com.example.sergey.shlypa2.views

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.dimen
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.getSmallImage
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.glide.CircleBorderTransform
import de.hdodenhof.circleimageview.CircleImageView

object HolderInflater {
    fun inflatePlayers(players: MutableList<Player>, context: Context, scoresMap: MutableMap<Long, Int>? = null): LinearLayout {
        val root = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
        }

        val margin = root.dimen(R.dimen.in_card_horizontal_margin)
        val smallMargin = root.dimen(R.dimen.small_margin)
        val avatarSize = root.dimen(R.dimen.player_in_team_avatar)
        val textSizeMed = root.dimen(R.dimen.text_smoll).toFloat()
        val textSizeBig = root.dimen(R.dimen.text_medium).toFloat()
        val textColor = ContextCompat.getColor(context, R.color.darkGrey)

        for (player in players) {
            val child = LinearLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                orientation = LinearLayout.HORIZONTAL
            }

            val avatarView = CircleImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(avatarSize,
                        avatarSize).apply {
                    leftMargin = 0
                    rightMargin = margin
                    topMargin = smallMargin
                    bottomMargin = smallMargin
                }
            }
            child.addView(avatarView)

            val tvName = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT).apply {
                    weight = 1F
                    gravity = Gravity.CENTER_VERTICAL
                }
                text = player.name
                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeMed)
                setTextColor(textColor)
            }
            child.addView(tvName)

            if (scoresMap != null) {
                val tvScores = TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        rightMargin = smallMargin
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeBig)
                    setTextColor(textColor)
                    text = scoresMap[player.id]?.toString() ?: "0"
                }
                child.addView(tvScores)
            }

            val borderColor = Functions.getThemedBgColor(root.context)
            Glide.with(root)
                    .load(player.getSmallImage(root.context))
                    .apply(RequestOptions()
                            .transforms(CircleCrop(), CircleBorderTransform(borderColor, 1.dpToPx)))
                    .into(avatarView)

            root.addView(child)
        }

        return root
    }
}