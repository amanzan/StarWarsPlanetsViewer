package com.luzia.starwarsplanetsviewer.data.model

import androidx.annotation.Keep

@Keep
data class PlanetDto(
    val name: String,
    val climate: String,
    val population: String,
    val diameter: String,
    val gravity: String,
    val terrain: String,
    val url: String
) {
    val id: Int
        get()  = url.split("/").dropLast(1).last().toInt()
}