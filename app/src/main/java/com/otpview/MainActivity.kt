package com.otpview

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import view.otpview.OTPView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val otpView: OTPView = findViewById(R.id.otpView)
        val b: Button = findViewById(R.id.button)
        otpView.setOnOtpSubmitListener { otp ->
            Toast.makeText(this, "Your Otp is: $otp", Toast.LENGTH_SHORT).show()
        }
        b.setOnClickListener {
            Toast.makeText(this, "Your Otp is: ${otpView.getOtpValue()}", Toast.LENGTH_SHORT).show()
        }
    }
}