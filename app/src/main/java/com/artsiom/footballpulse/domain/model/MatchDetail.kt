package com.artsiom.footballpulse.domain.model

data class Player(
    val name: String,
    val shirtNumber: Int?,
    val position: String?
)

data class Goal(
    val minute: Int,
    val injuryTime: Int?,
    val scorerName: String,
    val isHome: Boolean
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
    val goals: List<Goal>
)
