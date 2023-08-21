package com.example.baseproject.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.baseproject.model.local.data.GameEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for games data
 */
@Dao
interface GamesDao {

    @Query("SELECT * FROM $GAMES_TABLE")
    fun getAllGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gameEntities: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameEntity(gameEntity: GameEntity)
}