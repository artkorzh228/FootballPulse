package com.artsiom.footballpulse.data.remote

data class MatchDto(
    val id: Int,
    val utcDate: String,
    val status: String,
    val homeTeam: TeamDto,
    val awayTeam: TeamDto,
    val score: ScoreDto
)

