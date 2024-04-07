package com.example.qrscanner

/*import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

/*class SignUpActivity : AppCompatActivity() {
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
}*/

import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpUsername: EditText
    private lateinit var signUpEmail: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var signUpConPassword: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginRedirect: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        signUpUsername = findViewById(R.id.signupUsername)
        signUpEmail = findViewById(R.id.signupEmail)
        signUpPassword = findViewById(R.id.signupPassword)
        signUpConPassword = findViewById(R.id.signupConPassword)
        signUpButton = findViewById(R.id.signupButton)
        loginRedirect = findViewById(R.id.loginRedirect)

        signUpButton.setOnClickListener {
            val username = signUpUsername.text.toString()
            val email = signUpEmail.text.toString()
            val password = signUpPassword.text.toString()
            val conPassword = signUpConPassword.text.toString()

            // Check if passwords match
            if (password != conPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign up success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        // You can perform further actions after successful sign-up here
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                        // For example, navigate to another activity
                        // startActivity(Intent(this, AnotherActivity::class.java))
                    } else {
                        // If sign-up fails, display a message to the user.
                        Toast.makeText(baseContext, "Sign up failed. ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginRedirect.setOnClickListener {
            // Redirect to login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}*/
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import android.content.Intent


class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpUsername: EditText
    private lateinit var signUpEmail: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var signUpConPassword: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginRedirect: TextView
    private lateinit var signupProgressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        signUpUsername = findViewById(R.id.signupUsername)
        signUpEmail = findViewById(R.id.signupEmail)
        signUpPassword = findViewById(R.id.signupPassword)
        signUpConPassword = findViewById(R.id.signupConPassword)
        signUpButton = findViewById(R.id.signupButton)
        loginRedirect = findViewById(R.id.loginRedirect)
        signupProgressBar = findViewById(R.id.signupprogressBar)

        signUpButton.setOnClickListener {
            val user = signUpUsername.text.toString()
            val email = signUpEmail.text.toString()
            val password = signUpPassword.text.toString()
            val conPassword = signUpConPassword.text.toString()

            // Show ProgressBar
            signupProgressBar.visibility = View.VISIBLE

            // Check if passwords match
            if (password != conPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                // Hide ProgressBar
                signupProgressBar.visibility = View.GONE
                return@setOnClickListener
            }

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign up success
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                        // Redirect user to another activity or perform further actions
                    } else {
                        // Sign up failed
                        Toast.makeText(this, "Sign up failed. ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                    // Hide ProgressBar
                    signupProgressBar.visibility = View.GONE
                }
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

        }
    }
}

