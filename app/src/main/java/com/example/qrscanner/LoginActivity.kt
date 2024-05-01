package com.example.qrscanner
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider



class LoginActivity : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirect: TextView
    private lateinit var forgotPass: TextView
    private lateinit var loginProgressBar: ProgressBar
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gButton:Button

    private lateinit var auth: FirebaseAuth

    private var f = 0

    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        loginUsername = findViewById(R.id.loginUsername)
        loginPassword = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginButton)
        signupRedirect = findViewById(R.id.signupRedirect)
        loginProgressBar = findViewById(R.id.loginprogressBar)
        forgotPass = findViewById(R.id.forgotPass)
        gButton = findViewById(R.id.gButton)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish()
            return
        }

        loginButton.setOnClickListener {
            val email = loginUsername.text.toString()
            val password = loginPassword.text.toString()
            if(email==null || password==null){
                Toast.makeText(this,"enter details!..",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginProgressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, BottomNavigationActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                    loginProgressBar.visibility = View.GONE
                }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        firebaseAuthWithGoogle(account.idToken!!)
                        startActivity(Intent(this,HealthInfoActivity::class.java))
                    }
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "signInResult:failed code=${e.statusCode}")
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        gButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }

        signupRedirect.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        forgotPass.setOnClickListener {
            startActivity(Intent(this, forgotPass::class.java))
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // If sign in succeeds, update UI with the signed-in user's information
                    // ...
                    f = 1
                    startActivity(Intent(this, BottomNavigationActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
