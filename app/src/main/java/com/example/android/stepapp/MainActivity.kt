package com.example.android.stepapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.stepapp.goal.GoalsFragment
import com.example.android.stepapp.history.HistoryFragment
import com.example.android.stepapp.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private val homeFragment = HomeFragment()
private val historyFragment = HistoryFragment()
private val goalsFragment = GoalsFragment()


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //replaceFragment(homeFragment)

        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)

//        bottom_nav.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.mainFrag -> replaceFragment(homeFragment)
//                R.id.goalsFragment -> replaceFragment(goalsFragment)
//                R.id.historyFragment-> replaceFragment(historyFragment)
//            }
//            true
//        }

    }

    fun replaceFragment(fragment: Fragment){
        if (fragment!=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,fragment)
            transaction.commit()
        }

    }

}