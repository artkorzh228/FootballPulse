package com.artsiom.footballpulse.ui.matchdetails

import com.artsiom.footballpulse.domain.model.MatchDetail

sealed class MatchDetailsUiState {
    object Loading : MatchDetailsUiState()
    data class Success(val match: MatchDetail) : MatchDetailsUiState()
    data class Error(val message: String) : MatchDetailsUiState()
}
