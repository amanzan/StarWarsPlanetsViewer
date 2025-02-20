package com.luzia.starwarsplanetsviewer.domain.model

data class Planet(
    val id: Int,
    val name: String,
    val climate: String,
    val population: String,
    val diameter: String,
    val gravity: String,
    val terrain: String
)