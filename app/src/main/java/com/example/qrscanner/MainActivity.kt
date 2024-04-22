package com.example.qrscanner

import android.app.appsearch.SearchResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.util.ArrayList
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
//    var value:String?=intent.getStringExtra("abc")
    val url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCHhKB_3H4NyEIuTawShVFQ_ECOgPbc39M&cx=638f013618ba54b79&q=light_bulbs"
    lateinit var constview:androidx.constraintlayout.widget.ConstraintLayout
    lateinit var recview:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constview=findViewById(R.id.con)
        recview=constview.findViewById(R.id.recyview)
        recview.layoutManager = LinearLayoutManager(this)

        process_data()

    }

    fun process_data() {
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Handle successful response
                val searchResults = response.getJSONArray("items")
                val dataArray = ArrayList<SearchRes>()
                // Parse and process search results
                val builder=GsonBuilder()
                val gson= Gson()
                for (i in 0 until searchResults.length()) {
                    val jsonObject: JSONObject = searchResults.getJSONObject(i)
                    val searchResult: SearchRes = gson.fromJson(jsonObject.toString(), SearchRes::class.java)
                    dataArray.add(searchResult)
                }
                val adapter=myadapter(dataArray)
                recview.adapter=adapter
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                // Handle error
            }
        )
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

}