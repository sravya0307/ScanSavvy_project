package com.example.qrscanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val loginredirect=findViewById<TextView>(R.id.loginRedirect)
        loginredirect.setOnClickListener {
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}