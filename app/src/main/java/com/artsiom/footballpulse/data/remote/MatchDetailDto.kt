package com.artsiom.footballpulse.data.remote

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("id")         val id: Int?,
    @SerializedName("name")       val name: String,
    @SerializedName("position")   val position: String?,
    @SerializedName("shirtNumber") val shirtNumber: Int?
)

data class LineupTeamDto(
    @SerializedName("id")     val id: Int,
    @SerializedName("name")   val name: String,
    @SerializedName("lineup") val lineup: List<PlayerDto>?,
    @SerializedName("bench")  val bench: List<PlayerDto>?
)

data class GoalScorerDto(
    @SerializedName("id")   val id: Int?,
    @SerializedName("name") val name: String?
)

data class GoalTeamDto(
    @SerializedName("id")   val id: Int?,
    @SerializedName("name") val name: String?
)

data class GoalDto(
    @SerializedName("minute")     val minute: Int?,
    @SerializedName("injuryTime") val injuryTime: Int?,
    @SerializedName("type")       val type: String?,
    @SerializedName("team")       val team: GoalTeamDto?,
    @SerializedName("scorer")     val scorer: GoalScorerDto?
)

data class MatchDetailDto(
    @SerializedName("id")       val id: Int,
    @SerializedName("utcDate")  val utcDate: String,
    @SerializedName("status")   val status: String,
    @SerializedName("homeTeam") val homeTeam: LineupTeamDto,
    @SerializedName("awayTeam") val awayTeam: LineupTeamDto,
    @SerializedName("score")    val score: ScoreDto,
    @SerializedName("goals")    val goals: List<GoalDto>?
)
