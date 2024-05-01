package com.example.qrscanner

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HealthInfoActivity : AppCompatActivity() {
    lateinit var selectCard: MaterialCardView
    lateinit var diseases: TextView
    lateinit var name: EditText
    lateinit var age: EditText
    lateinit var height: EditText
    lateinit var weight: EditText
    lateinit var selectedDiseases: BooleanArray
    private lateinit var save: Button
    private lateinit var skip: Button
    private lateinit var auth: FirebaseAuth

    val diseaseList = ArrayList<Int>()
    val diseaseArray = arrayOf("Diabetes", "Blood Pressure", "Cholesterol", "Hemoglobin deficiency")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_info)

        auth = FirebaseAuth.getInstance()

        selectCard = findViewById(R.id.selectCard)
        diseases = findViewById(R.id.SelectDiseases)
        selectedDiseases = BooleanArray(diseaseArray.size)
        selectCard.setOnClickListener {
            showDiseaseDialog()
        }
        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        height = findViewById(R.id.height)
        weight = findViewById(R.id.weight)
        save = findViewById(R.id.savee)
        skip = findViewById(R.id.skipp)
        save.setOnClickListener {
            val email = auth.currentUser?.email
            if (email != null) {
                writeUserDataToFirestore(email)
                Toast.makeText(this, "Save successful!..", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, BottomNavigationActivity::class.java)
                startActivity(intent)
            }
        }
        skip.setOnClickListener {
            Toast.makeText(this, "You can enter the details later...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish()
        }
    }

    private fun writeUserDataToFirestore(email: String) {
        val firestore = FirebaseFirestore.getInstance()
        val heightValue = height.text.toString().toDoubleOrNull()
        val weightValue = weight.text.toString().toDoubleOrNull()

        if (heightValue != null && weightValue != null) {
            val userData = hashMapOf(
                "email" to email,
                "name" to name.text.toString(),
                "age" to age.text.toString(),
                "height" to heightValue,
                "weight" to weightValue,
                "diseases" to diseaseList.map { diseaseArray[it] }
            )
            firestore.collection("healthinfo")
                .document(auth.currentUser?.uid ?: "")
                .set(userData)
                .addOnSuccessListener {
                    Log.d("FirebaseManager", "Data saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseManager", "Failed to save data: $e")
                }
        } else {
            Log.e("FirebaseManager", "Invalid height or weight")
        }
    }

    private fun showDiseaseDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Diseases:")
        builder.setCancelable(false)
        builder.setMultiChoiceItems(
            diseaseArray,
            selectedDiseases
        ) { dialog, which, isChecked ->
            if (isChecked) {
                diseaseList.add(which)
            } else {
                diseaseList.remove(which)
            }
        }.setPositiveButton("Ok") { dialog, which ->
            val stringBuilder = StringBuilder()
            for (i in 0 until diseaseList.size) {
                stringBuilder.append(diseaseArray[diseaseList[i]])
                if (i != diseaseList.size - 1) {
                    stringBuilder.append(", ")
                }
                diseases.text = stringBuilder.toString()
            }
        }.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }.setNeutralButton("Clear all") { dialog, which ->
            for (i in 0 until selectedDiseases.size) {
                selectedDiseases[i] = false
            }
            diseaseList.clear()
            diseases.text = ""
        }
        builder.show()
    }
}
