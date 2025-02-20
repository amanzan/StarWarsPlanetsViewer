package com.luzia.starwarsplanetsviewer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luzia.starwarsplanetsviewer.data.model.PlanetEntity

@Dao
interface PlanetDao {
    @Query("SELECT * FROM planets")
    suspend fun getAllPlanets(): List<PlanetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanets(planets: List<PlanetEntity>)

    @Query("DELETE FROM planets")
    suspend fun clearPlanets()

    @Query("SELECT * FROM planets WHERE id = :planetId")
    suspend fun getPlanetById(planetId: Int): PlanetEntity?
}