package com.luzia.starwarsplanetsviewer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planets")
data class PlanetEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val climate: String,
    val population: String,
    val diameter: String,
    val gravity: String,
    val terrain: String
)