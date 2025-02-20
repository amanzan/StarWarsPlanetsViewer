package com.luzia.starwarsplanetsviewer.di

import android.content.Context
import androidx.room.Room
import com.luzia.starwarsplanetsviewer.data.local.PlanetDao
import com.luzia.starwarsplanetsviewer.data.local.PlanetDatabase
import com.luzia.starwarsplanetsviewer.data.local.PlanetLocalDataSource
import com.luzia.starwarsplanetsviewer.data.remote.PlanetApi
import com.luzia.starwarsplanetsviewer.data.remote.PlanetRemoteDataSource
import com.luzia.starwarsplanetsviewer.data.repository.PlanetRepositoryImpl
import com.luzia.starwarsplanetsviewer.domain.repository.PlanetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlanetApi(): PlanetApi {
        return Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlanetApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlanetDatabase(@ApplicationContext context: Context): PlanetDatabase {
        return Room.databaseBuilder(
            context,
            PlanetDatabase::class.java,
            "planet_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlanetDao(database: PlanetDatabase): PlanetDao {
        return database.planetDao()
    }

    @Provides
    @Singleton
    fun providePlanetRepository(
        remoteDataSource: PlanetRemoteDataSource,
        localDataSource: PlanetLocalDataSource
    ): PlanetRepository {
        return PlanetRepositoryImpl(remoteDataSource, localDataSource)
    }
}