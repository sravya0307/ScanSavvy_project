package com.example.qrscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class forgotpass : AppCompatActivity() {
    private lateinit var submit:Button
    private lateinit var text1: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass)
        submit = findViewById(R.id.submit)
        text1 = findViewById(R.id.text1)
        submit.setOnClickListener{
            val email:String = text1.text.toString().trim{ it <= ' ' }
            if(email.isEmpty()){
                Toast.makeText(
                    this@forgotpass,
                    "Please Enter Email Address.",
                    Toast.LENGTH_SHORT
                ).show()
                }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this@forgotpass,
                                "Email sent successfully to reset your password!",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                        else{
                            Toast.makeText( this@forgotpass,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}