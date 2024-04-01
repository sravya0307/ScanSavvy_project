package com.example.qrscanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginButton=findViewById<Button>(R.id.loginButton)
        val signupredirect=findViewById<TextView>(R.id.signupRedirect)
        signupredirect.setOnClickListener {
            val intent= Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginButton.setOnClickListener {
            val intent= Intent(this, BottomNavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}