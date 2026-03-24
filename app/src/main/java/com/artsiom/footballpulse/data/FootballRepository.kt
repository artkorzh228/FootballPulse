package com.artsiom.footballpulse.data

import com.artsiom.footballpulse.data.remote.MatchesResponse
import com.artsiom.footballpulse.data.remote.RetrofitInstance
import com.artsiom.footballpulse.domain.model.Match

class FootballRepository(){
    suspend fun getMatches(leagueCode: String): List<Match> {
        val response = RetrofitInstance.api.getMatches(leagueCode)
        val matches = response.body()?.matches ?: emptyList()
        return matches.map { dto ->
            Match(
                id = dto.id,
                homeTeam = dto.homeTeam.name,
                awayTeam = dto.awayTeam.name,
                date = dto.utcDate,
                status = dto.status,
                homeScore = dto.score.fullTime.home,
                awayScore = dto.score.fullTime.away
            )
        }
    }
}