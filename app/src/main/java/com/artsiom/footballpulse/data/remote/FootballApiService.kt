package com.artsiom.footballpulse.data.remote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FootballApiService {
    @GET("v4/competitions/{code}/matches")
    suspend fun getMatches(@Path("code") leagueCode: String) : Response<MatchesResponse>
}