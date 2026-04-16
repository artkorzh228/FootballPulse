package com.artsiom.footballpulse.ui.standings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artsiom.footballpulse.data.FootballRepository
import com.artsiom.footballpulse.domain.model.League
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StandingsViewModel : ViewModel() {

    private val repository = FootballRepository()

    val leagues = listOf(
        League("PL",  "Premier League", "PL"),
        League("PD",  "La Liga",        "La Liga"),
        League("BL1", "Bundesliga",     "BL"),
        League("FL1", "Ligue 1",        "L1"),
        League("SA",  "Serie A",        "SA"),
    )

    private val _currentLeagueCode = MutableStateFlow("PL")
    val currentLeagueCode: StateFlow<String> = _currentLeagueCode

    private val _uiState = MutableStateFlow<StandingsUiState>(StandingsUiState.Loading)
    val uiState: StateFlow<StandingsUiState> = _uiState

    private val _viewMode = MutableStateFlow(ViewMode.FULL)
    val viewMode: StateFlow<ViewMode> = _viewMode

    init {
        fetchStandings()
    }

    /** Switch league tab. Re-fetches only if league changed or data not yet loaded. */
    fun loadLeague(code: String) {
        if (_currentLeagueCode.value == code && _uiState.value is StandingsUiState.Success) return
        _currentLeagueCode.value = code
        fetchStandings()
    }

    /** Switch view mode without re-fetching data. */
    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }

    fun retry() {
        fetchStandings()
    }

    private fun fetchStandings() {
        viewModelScope.launch {
            _uiState.value = StandingsUiState.Loading
            try {
                val standings = repository.getStandings(_currentLeagueCode.value)
                standings.firstOrNull()?.let { team ->
                    Log.d("STANDINGS", "form field = ${team.form}")
                }
                _uiState.value = StandingsUiState.Success(standings)
            } catch (e: Exception) {
                _uiState.value = StandingsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
