package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.appodeal.ads.Appodeal
import com.appodeal.ads.NativeAd
import com.appodeal.ads.NativeCallbacks
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_turn_start.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class TurnStartFragment : Fragment() {

    lateinit var viewModel: RoundViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_turn_start, container, false)

        val playerTv: TextView = root.findViewById(R.id.tvTurnPlayerName)
        val startButton: Button = root.findViewById(R.id.btTurnStart)
        val playerAvatar: CircleImageView = root.findViewById(R.id.civPlayerAvatar)
        val teamName: TextView = root.findViewById(R.id.tv_TurnTeamName)

        teamName.text = viewModel.getTeam()

        playerTv.text = viewModel.getPlayer().name
        startButton.setOnClickListener { viewModel.startTurn() }

        Picasso.get()
                .load(Functions.imageNameToUrl("player_avatars/large/${viewModel.getPlayer().avatar}"))
                .into(playerAvatar)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_show_hint) {

            viewModel.loadHintTeam()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        Appodeal.setNativeCallbacks(object : NativeCallbacks {
            override fun onNativeLoaded() {
                Timber.d("On native loaded")
            }

            override fun onNativeFailedToLoad() {
                Timber.d("On native failed to load")
            }

            override fun onNativeShown(nativeAd: NativeAd) {
                Timber.d("On native shown")
            }

            override fun onNativeClicked(nativeAd: NativeAd) {
                Timber.d("On native clicked")
            }
        })

        val nativeAds = Appodeal.getNativeAds(1)
        nativeAds.getOrNull(0)?.let {
            nativeAd.setNativeAd(it)
        }


    }
}
