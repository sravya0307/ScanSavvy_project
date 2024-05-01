package com.example.qrscanner


import com.google.firebase.firestore.FirebaseFirestore

import com.google.android.gms.common.api.ApiException

import android.util.Log
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import tempo


class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpUsername: EditText
    private lateinit var signUpEmail: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var signUpConPassword: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginRedirect: TextView
    private lateinit var signupProgressBar: ProgressBar
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gButton: Button
    private val REQ_ONE_TAP = 297  // Can be any integer unique to the Activity
    private var showOneTapUI = true


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val RC_SIGN_IN = 9001

        auth = FirebaseAuth.getInstance()
        signUpUsername = findViewById(R.id.signupUsername)
        signUpEmail = findViewById(R.id.signupEmail)
        signUpPassword = findViewById(R.id.signupPassword)
        signUpConPassword = findViewById(R.id.signupConPassword)
        signUpButton = findViewById(R.id.signupButton)
        loginRedirect = findViewById(R.id.loginRedirect)
        signupProgressBar = findViewById(R.id.signupprogressBar)
        gButton = findViewById(R.id.gButton)
        val currentUser = FirebaseAuth.getInstance().currentUser
        //val firestore = FirebaseFirestore.getInstance()
        //val usersCollection = firestore.collection("healthinfo")
        if (currentUser != null) {
            startActivity(
                Intent(
                    this,
                    BottomNavigationActivity::class.java
                )
            ) // Launch emergency contact dialog
            finish() // finish the login activity so user can't go back
            return
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        gButton.setOnClickListener {
            if (googleSignInClient == null) {
                googleSignInClient = GoogleSignIn.getClient(this, gso)
            }
            val intent = googleSignInClient!!.signInIntent
            signInLauncher.launch(intent)
            if(flag==1){
                startActivity(Intent(this,HealthInfoActivity::class.java))
            }
            else{
                Toast.makeText(this,"Error in using using google .Try signin with email and password",Toast.LENGTH_SHORT).show()
            }

        }









        signUpButton.setOnClickListener {
            val user = signUpUsername.text.toString()
            val email = signUpEmail.text.toString()
            val password = signUpPassword.text.toString()
            val conPassword = signUpConPassword.text.toString()
            if (user.isEmpty() || email.isEmpty() || password.isEmpty() || conPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Exit the click listener if any field is empty
            }

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
                        val user = task.result?.user
                        if (user != null) {

                            writeEmailtoCollection(email)
                             val x=tempo()
                            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HealthInfoActivity::class.java)
                            // Redirect user to another activity or perform further actions
                            startActivity(intent)
                            finish()
                        } else {
                            // Sign up failed
                            Toast.makeText(
                                this, "Sign up failed. ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
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


        private var flag=0
        private val signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    flag=1
                    val account = task.result
                    handleSignedInAccount(account)

                } catch (e: ApiException) {
                    // Sign in failed
                    //Log.w("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
                    // Handle sign-in failure (e.g., show a snackbar with an error message)
                }
            }
        }

        private fun handleSignedInAccount(account: GoogleSignInAccount) {
            val idToken = account.idToken
            val email = account.email

            // Send the ID token to your server for authentication (if applicable)
            // Use the email address for further processing (e.g., display user information)
            // Update UI to reflect signed-in state (optional)
        }
    private fun writeEmailtoCollection(email: String){
        val firestore = FirebaseFirestore.getInstance()
        val userCollection = firestore.collection("healthinfo")
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.uid?.let { uid ->
            val user = hashMapOf(
                "email" to email
            )

            userCollection.document(uid).set(user)
                .addOnSuccessListener {
                    Log.d("FirebaseManager", "Success")
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseManager", "Failure: $e")
                }
        } ?: run {
            Log.e("FirebaseManager", "Current user is null.")
        }
    }


}
