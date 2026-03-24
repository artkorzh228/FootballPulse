package com.artsiom.footballpulse.ui.matches

import com.artsiom.footballpulse.domain.model.Match

sealed interface MatchesUiState {
    object Loading : MatchesUiState
    data class Success(val matches: List<Match>) : MatchesUiState
    data class Error(val message: String) : MatchesUiState

}