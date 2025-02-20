package com.luzia.starwarsplanetsviewer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luzia.starwarsplanetsviewer.data.model.PlanetEntity

@Database(
    entities = [PlanetEntity::class],
    version = 1
)
abstract class PlanetDatabase : RoomDatabase() {
    abstract fun planetDao(): PlanetDao
}
