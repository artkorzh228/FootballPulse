package com.artsiom.footballpulse

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.artsiom.footballpulse.ui.matchdetails.MatchDetailsFragment
import com.artsiom.footballpulse.ui.teamdetails.TeamDetailsFragment
import com.artsiom.footballpulse.ui.matches.MatchesFragment
import com.artsiom.footballpulse.ui.standings.StandingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "settings"
        private const val KEY_DARK_MODE = "dark_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isDark = prefs.getBoolean(KEY_DARK_MODE, false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnThemeToggle = findViewById<FloatingActionButton>(R.id.btnThemeToggle)
        val isNightNow = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        btnThemeToggle.setImageResource(if (isNightNow) R.drawable.ic_sun else R.drawable.ic_moon)

        btnThemeToggle.setOnClickListener {
            val isDarkNow = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            if (isDarkNow) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefs.edit().putBoolean(KEY_DARK_MODE, false).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefs.edit().putBoolean(KEY_DARK_MODE, true).apply()
            }
        }

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

    fun navigateToTeamDetails(teamId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TeamDetailsFragment.newInstance(teamId))
            .addToBackStack(null)
            .commit()
    }
}
