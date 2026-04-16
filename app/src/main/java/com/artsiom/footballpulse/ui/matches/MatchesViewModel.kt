package com.artsiom.footballpulse.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.artsiom.footballpulse.data.FootballRepository
import com.artsiom.footballpulse.domain.model.League

class MatchesViewModel : ViewModel() {

    private val repository = FootballRepository()

    private val _uiState = MutableStateFlow<MatchesUiState>(MatchesUiState.Loading)
    val uiState: StateFlow<MatchesUiState> = _uiState

    val leagues = listOf(
        League("PL",  "Premier League", "PL"),
        League("PD",  "La Liga",        "La Liga"),
        League("BL1", "Bundesliga",     "BL"),
        League("FL1", "Ligue 1",        "L1"),
        League("SA",  "Serie A",        "SA"),
    )

    val leagueMaxMatchdays = mapOf(
        "PL"  to 38,
        "PD"  to 38,
        "BL1" to 34,
        "FL1" to 34,
        "SA"  to 38
    )

    private val _currentMatchday = MutableStateFlow(1)
    val currentMatchday: StateFlow<Int> = _currentMatchday

    private val _currentLeagueCode = MutableStateFlow("PL")
    val currentLeagueCode: StateFlow<String> = _currentLeagueCode

    private var currentSeason: Int = 2025 // updated on first successful load

    init {
        loadLeague("PL")
    }

    /** Called when the user taps a league tab. */
    fun loadLeague(leagueCode: String) {
        _currentLeagueCode.value = leagueCode
        viewModelScope.launch {
            _uiState.value = MatchesUiState.Loading
            try {
                val result = repository.getCurrentMatchdayResult(leagueCode)
                currentSeason = result.season
                _currentMatchday.value = result.matchday
                _uiState.value = MatchesUiState.Success(result.matches)
            } catch (e: Exception) {
                _uiState.value = MatchesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun goToPreviousMatchday() {
        val prev = (_currentMatchday.value - 1).coerceAtLeast(1)
        if (prev != _currentMatchday.value) {
            _currentMatchday.value = prev
            fetchMatchday(prev)
        }
    }

    fun goToNextMatchday() {
        val max = leagueMaxMatchdays[_currentLeagueCode.value] ?: 38
        val next = (_currentMatchday.value + 1).coerceAtMost(max)
        if (next != _currentMatchday.value) {
            _currentMatchday.value = next
            fetchMatchday(next)
        }
    }

    private fun fetchMatchday(matchday: Int) {
        viewModelScope.launch {
            _uiState.value = MatchesUiState.Loading
            try {
                val matches = repository.getMatchesByMatchday(
                    _currentLeagueCode.value, matchday, currentSeason
                )
                _uiState.value = MatchesUiState.Success(matches)
            } catch (e: Exception) {
                _uiState.value = MatchesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
