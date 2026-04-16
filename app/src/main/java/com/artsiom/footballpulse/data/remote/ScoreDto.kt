package com.artsiom.footballpulse.data.remote

class ScoreDto(
    val fullTime: FullTimeDto,
    val halfTime: HalfTimeDto?
)

class FullTimeDto(
    val home: Int?,
    val away: Int?
)

class HalfTimeDto(
    val home: Int?,
    val away: Int?
)