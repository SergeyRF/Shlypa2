package com.example.sergey.shlypa2.screens.game


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.getLargeImage
import com.example.sergey.shlypa2.extensions.observeOnce
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.show
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.fragment_turn_start.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class TurnStartFragment : Fragment() {

    private val viewModel: RoundViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_turn_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.roundLiveData.observeSafe(this) { round ->
            tvTeamName.text = round.currentTeam.name
            tvTurnPlayerName.text = round.getPlayer().name
            Glide.with(this)
                    .load(round.getPlayer().getLargeImage(requireContext()))
                    .into(civPlayerAvatar)
        }

        viewModel.nativeAdLiveData.observeOnce(this, ::onNativeAd)

        btTurnStart.setOnClickListener { viewModel.startTurn() }
    }

    private fun onNativeAd(ad: UnifiedNativeAd) {
        adsTurnStart.show()
        adsTurnStart.setNativeAd(ad)
        viewModel.nativeAdShown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adsTurnStart.destroyNativeAd()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_show_hint -> {
                viewModel.showScoresTable()
                true
            }
            else -> false
        }
    }
}
