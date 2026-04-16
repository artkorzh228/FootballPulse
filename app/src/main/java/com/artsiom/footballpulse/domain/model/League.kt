package com.artsiom.footballpulse.domain.model

data class League(
    val code: String,
    val name: String,
    val shortName: String = code
)
