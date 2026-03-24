package com.artsiom.footballpulse.data.remote

class ScoreDto(
    val fullTime: FullTimeDto
) {}

class FullTimeDto(
    val home: Int?,
    val away: Int?
)
{}