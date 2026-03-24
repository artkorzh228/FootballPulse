package com.artsiom.footballpulse.ui.matches

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.artsiom.footballpulse.data.FootballRepository
import com.artsiom.footballpulse.domain.model.League

class MatchesViewModel: ViewModel() {
    private val _uiState = MutableStateFlow<MatchesUiState>(MatchesUiState.Loading)
    val uiState: StateFlow<MatchesUiState> = _uiState

    val leagues = listOf(
        League("PL", "Premier League"),
        League("PD", "La Liga"),
        League("BL1", "Bundesliga"),
        League("FL1", "Ligue 1"),
        League("SA", "Serie A"),
    )

    fun loadMatches(leagueCode: String = "PL") {
        viewModelScope.launch {
            _uiState.value = MatchesUiState.Loading
            try {
                val repository = FootballRepository()
                val matches = repository.getMatches(leagueCode)
                _uiState.value = MatchesUiState.Success(matches)
            }
            catch (e: Exception) {
                _uiState.value = MatchesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}