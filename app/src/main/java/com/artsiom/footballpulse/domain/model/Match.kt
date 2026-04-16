package com.artsiom.footballpulse.domain.model

data class Match(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val date: String,
    val status: String,
    val homeTeamCrest: String?,
    val awayTeamCrest: String?
)
