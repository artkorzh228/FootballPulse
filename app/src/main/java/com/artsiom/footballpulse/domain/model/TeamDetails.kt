package com.artsiom.footballpulse.domain.model

data class SquadPlayer(
    val id: Int?,
    val name: String,
    val position: String?,
    val nationality: String?,
    val shirtNumber: Int?
)

data class TeamDetails(
    val id: Int,
    val name: String,
    val shortName: String?,
    val tla: String?,
    val crest: String?,
    val founded: Int?,
    val venue: String?,
    val clubColors: String?,
    val squad: List<SquadPlayer>
)
