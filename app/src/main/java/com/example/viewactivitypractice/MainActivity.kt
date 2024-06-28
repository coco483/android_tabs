package com.example.viewactivitypractice

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(){

    private lateinit var tab1: Fragment
    private lateinit var tab2: Fragment
    private lateinit var tab3: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.bottom_navigating_home)

        tab1 = Tab1()
        tab2 = Tab2()
        tab3 = Tab3()

        supportFragmentManager.beginTransaction().replace(R.id.container, tab1).commit()

        val bottomnavigationView: NavigationBarView = findViewById(R.id.bottom_navigation)
        bottomnavigationView.setOnItemSelectedListener (
            object: NavigationBarView.OnItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    var selectedFragment: Fragment?= null
                    when(item.itemId) {
                        R.id.tab1 -> selectedFragment = tab1
                        R.id.tab2 -> selectedFragment = tab2
                        R.id.tab3 -> selectedFragment = tab3
                    }
                    selectedFragment?.let {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, selectedFragment)
                            .commit()
                        return true
                    }
                    return false
                }
            }
        )
    }
}