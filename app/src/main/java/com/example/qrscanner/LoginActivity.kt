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

import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class LoginActivity : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var gButton: Button
    private lateinit var signupRedirect: TextView
    private lateinit var forgotPass: TextView
    private lateinit var loginProgressBar: ProgressBar
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth
    private lateinit var gso:GoogleSignInOptions


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
        gButton=findViewById(R.id.gButton)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            // User is already signed in with Google, navigate to BottomNavigationActivity
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish() // Finish LoginActivity to prevent user from going back
        }


        gButton.setOnClickListener{
            if (googleSignInClient == null) {
                googleSignInClient = GoogleSignIn.getClient(this, gso)
            }
            val intent = googleSignInClient!!.signInIntent
            signInLauncher.launch(intent)

        }
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
                        val intent= Intent(this, BottomNavigationActivity::class.java)
                        startActivity(intent)

                        finish()
                        // Redirect user to another activity or perform further actions

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
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                handleSignedInAccount(account)

            } catch (e: ApiException) {
                // Sign in failed
                //Log.w("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
                // Handle sign-in failure (e.g., show a snackbar with an error message)
            }

        }
    }
    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, navigate to BottomNavigationActivity
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish() // Finish LoginActivity to prevent user from going back
        }


    }

    private fun handleSignedInAccount(account: GoogleSignInAccount?) {
        if (account != null) {
            val idToken = account.idToken
            val email = account.email

            // Send the ID token to your server for authentication (if applicable)
            // Use the email address for further processing (e.g., display user information)

            // Redirect user to BottomNavigationActivity
            val intent = Intent(this, BottomNavigationActivity::class.java)
            startActivity(intent)
            finish() // Finish LoginActivity to prevent user from going back
        }
    }





}