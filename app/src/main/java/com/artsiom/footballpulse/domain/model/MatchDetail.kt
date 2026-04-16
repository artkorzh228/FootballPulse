package com.artsiom.footballpulse.domain.model

data class Player(
    val name: String,
    val shirtNumber: Int?,
    val position: String?
)

data class MatchDetail(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val date: String,
    val status: String,
    val homeLineup: List<Player>,
    val awayLineup: List<Player>,
    val homeBench: List<Player>,
    val awayBench: List<Player>,
    val halfTimeHome: Int?,
    val halfTimeAway: Int?,
    val fullTimeHome: Int?,
    val fullTimeAway: Int?,
    val homeTeamCrest: String?,
    val awayTeamCrest: String?
)
