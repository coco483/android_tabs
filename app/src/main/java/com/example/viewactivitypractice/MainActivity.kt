package com.example.viewactivitypractice

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.viewactivitypractice.fragments.ContactTab
import com.example.viewactivitypractice.fragments.GalleryTab
import com.example.viewactivitypractice.fragments.DiaryTab
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(){

    private lateinit var tab1: Fragment
    private lateinit var tab2: Fragment
    private lateinit var tab3: Fragment
    lateinit var mydb: DataBaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.bottom_navigating_home)
        Log.d("MainActivity", "main activity created")

        tab1 = ContactTab()
        tab2 = GalleryTab()
        tab3 = DiaryTab()
        mydb = DataBaseHandler(this)
        //mydb.insertContact("김김김", "01032343544")
        //mydb.insertContact("고양이", "01048483929")
        // 처음에는 tab 1 화면이 등장
        supportFragmentManager.beginTransaction().replace(R.id.container, tab1).commit()

        val bottomnavigationView: NavigationBarView = findViewById(R.id.bottom_navigation)
        // bottom navigation 클릭에 따라 fragment 변경
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