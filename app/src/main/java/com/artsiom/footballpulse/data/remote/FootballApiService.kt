package com.artsiom.footballpulse.data.remote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FootballApiService {
    @GET("v4/competitions/{code}/matches")
    suspend fun getMatches(
        @Path("code") leagueCode: String,
        @Query("season") season: Int
    ): Response<MatchesResponse>

    @GET("v4/competitions/{code}/matches")
    suspend fun getMatchesByMatchday(
        @Path("code") leagueCode: String,
        @Query("matchday") matchday: Int,
        @Query("season") season: Int
    ): Response<MatchesResponse>

    @GET("v4/competitions/{code}/standings")
    suspend fun getStandings(@Path("code") leagueCode: String) : Response<StandingsResponse>

    @GET("v4/matches/{id}")
    suspend fun getMatchDetail(@Path("id") matchId: Int): Response<MatchDetailDto>

    @GET("v4/teams/{id}")
    suspend fun getTeam(@Path("id") id: Int): Response<TeamDetailsDto>
}