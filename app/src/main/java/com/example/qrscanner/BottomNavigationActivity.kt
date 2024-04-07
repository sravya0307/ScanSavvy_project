package com.example.qrscanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toolbar
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class BottomNavigationActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView:BottomNavigationView

    val scanFragment=ScanFragment()
    val historyFragment=HistoryFragment()
    val wishlistFragment=WishlistFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id)
        val iconprofile=findViewById<ImageView>(R.id.profile_icon)
        iconprofile.setOnClickListener {
            val intent= Intent(this, HealthInfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,scanFragment).commit()
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scan -> {
                    val selectedFragment = ScanFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, selectedFragment).commit()
                    true
                }
                R.id.history -> {
                    val selectedFragment = HistoryFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, selectedFragment).commit()
                    true
                }
                R.id.wishlist -> {
                    val selectedFragment = WishlistFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, selectedFragment).commit()
                    true
                }
                else -> false
            }
        }


    }
}