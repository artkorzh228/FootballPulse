package com.artsiom.footballpulse.domain.model

class League(
    val code: String,
    val name: String,
    val shortName: String = code
) {}
