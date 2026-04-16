package com.artsiom.footballpulse.data.remote

import com.google.gson.annotations.SerializedName

data class SquadPlayerDto(
    @SerializedName("id")          val id: Int?,
    @SerializedName("name")        val name: String,
    @SerializedName("position")    val position: String?,
    @SerializedName("nationality") val nationality: String?,
    @SerializedName("shirtNumber") val shirtNumber: Int?
)

data class TeamDetailsDto(
    @SerializedName("id")          val id: Int,
    @SerializedName("name")        val name: String,
    @SerializedName("shortName")   val shortName: String?,
    @SerializedName("tla")         val tla: String?,
    @SerializedName("crest")       val crest: String?,
    @SerializedName("founded")     val founded: Int?,
    @SerializedName("venue")       val venue: String?,
    @SerializedName("clubColors")  val clubColors: String?,
    @SerializedName("squad")       val squad: List<SquadPlayerDto>?
)
