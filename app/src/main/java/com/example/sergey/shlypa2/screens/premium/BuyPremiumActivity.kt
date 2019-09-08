package com.example.sergey.shlypa2.screens.premium

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.R
import kotlinx.android.synthetic.main.activity_buy_premium.*

class BuyPremiumActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, BuyPremiumActivity::class.java)
    }

    private var buyClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_premium)

        btBuy.setOnClickListener {
         onOkClicked()
        }

        btCancelBuy.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun onOkClicked() {
        if(buyClicked) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            tvPremium.setText(R.string.we_dont_have_premium)
            buyClicked = true
        }
    }
}
