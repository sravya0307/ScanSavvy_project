package com.example.qrscanner

import android.app.AlertDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView


class HealthInfoActivity : AppCompatActivity() {
        lateinit var selectCard: MaterialCardView
        lateinit var diseases: TextView
        lateinit var selectedDiseases: BooleanArray
        val diseaseList = ArrayList<Int>()
        val diseaseArray =
            arrayOf("Diabetes", "BloodPressure", "Cholestrol", "Heamoglobin deficiency")

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_health_info)


            // Sample items for the spinner
            selectCard = findViewById(R.id.selectCard)
            diseases = findViewById(R.id.SelectDiseases)
            selectedDiseases = BooleanArray(diseaseArray.size)
            selectCard.setOnClickListener {
                showDiseaseDialog()
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
                    diseases.setText(stringBuilder.toString())
                }
            }.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }.setNeutralButton("Clear all") { dialog, which ->
                for (i in 0 until selectedDiseases.size) {
                    selectedDiseases[i] = false
                }
                diseaseList.clear()
                diseases.setText("")
            }
            builder.show()
        }
    }
