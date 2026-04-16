package com.artsiom.footballpulse.domain.model

data class Standing(
    val position: Int,
    val teamName: String,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
    val form: String?
)