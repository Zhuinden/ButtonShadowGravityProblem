package com.acme.debugbuttonproject

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        loadingGameDetails.hide()
        doubleButtons.show()
        buttonLeft.setBackgroundResource(R.drawable.blue_button_enabled)
        buttonRight.setBackgroundResource(R.drawable.blue_button_enabled)
        buttonLeft.setTextColor(ContextCompat.getColor(this, R.color.white))
        buttonRight.setTextColor(ContextCompat.getColor(this, R.color.white))
        buttonLeft.lineColor = ContextCompat.getColor(this, R.color.button_line_blue)
        buttonLeft.onClick {
            showToast("Left")
        }
        buttonRight.lineColor = ContextCompat.getColor(this, R.color.button_line_blue)
        buttonRight.onClick {
            showToast("Right")
        }

        buttonClose.onClick {
            finish()
        }
    }
}