package com.artsiom.footballpulse.data.remote

data class TableEntryDto(
    val position: Int,
    val team: TeamDto,
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

data class StandingDto(
    val table: List<TableEntryDto>
)

data class StandingsResponse(
    val standings: List<StandingDto>
)

