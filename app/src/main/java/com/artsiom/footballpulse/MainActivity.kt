package com.artsiom.footballpulse

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artsiom.footballpulse.ui.matches.LeaguesAdapter
import com.artsiom.footballpulse.ui.matches.MatchesAdapter
import com.artsiom.footballpulse.ui.matches.MatchesUiState
import com.artsiom.footballpulse.ui.matches.MatchesViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val errorText = findViewById<TextView>(R.id.errorText)
        val leaguesRecyclerView = findViewById<RecyclerView>(R.id.leaguesRecyclerView)

        val matchesAdapter = MatchesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = matchesAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        val viewModel = ViewModelProvider(this)[MatchesViewModel::class.java]

        val leaguesAdapter = LeaguesAdapter { league ->
            android.util.Log.d("FootballPulse", "Lambda called: ${league.code}")
            viewModel.loadMatches(league.code)
        }
        leaguesRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        leaguesRecyclerView.adapter = leaguesAdapter
        leaguesAdapter.submitList(viewModel.leagues)

        viewModel.loadMatches()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is MatchesUiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            errorText.visibility = View.GONE
                        }
                        is MatchesUiState.Success -> {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            errorText.visibility = View.GONE
                            matchesAdapter.submitList(state.matches)
                        }
                        is MatchesUiState.Error -> {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.GONE
                            errorText.visibility = View.VISIBLE
                            errorText.text = state.message
                        }
                    }
                }
            }
        }
    }
}