package com.example.qrscanner
import android.content.Intent
//import com.yourpackage.name.SignUpActivity // Import SignUpActivity from your package

/*class LoginActivity : AppCompatActivity() {
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
class LoginActivity : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirect: TextView
    private lateinit var forgotPass: TextView
    private lateinit var loginProgressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        loginUsername = findViewById(R.id.loginUsername)
        loginPassword = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginButton)
        signupRedirect = findViewById(R.id.signupRedirect)
        loginProgressBar = findViewById(R.id.loginprogressBar)
        forgotPass=findViewById(R.id.forgotPass)

        loginButton.setOnClickListener {
            val email = loginUsername.text.toString()
            val password = loginPassword.text.toString()

            // Show ProgressBar
            loginProgressBar.visibility = View.VISIBLE

            // Authenticate user with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Redirect user to another activity or perform further actions
                        val intent = Intent(this,HealthInfoActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Sign in failed
                        Toast.makeText(this, "Login failed. ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                    // Hide ProgressBar
                    loginProgressBar.visibility = View.GONE
                }
        }

        signupRedirect.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        forgotPass.setOnClickListener{
            startActivity(Intent(this,forgotpass::class.java))
        }
    }
}
