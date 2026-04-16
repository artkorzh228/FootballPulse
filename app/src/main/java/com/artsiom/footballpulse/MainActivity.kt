package com.artsiom.footballpulse

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.artsiom.footballpulse.ui.matchdetails.MatchDetailsFragment
import com.artsiom.footballpulse.ui.matches.MatchesFragment
import com.artsiom.footballpulse.ui.standings.StandingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceFragment(MatchesFragment())
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_matches -> { replaceFragment(MatchesFragment()); true }
                R.id.nav_standings -> { replaceFragment(StandingsFragment()); true }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun navigateToMatchDetails(matchId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MatchDetailsFragment.newInstance(matchId))
            .addToBackStack(null)
            .commit()
    }
}
