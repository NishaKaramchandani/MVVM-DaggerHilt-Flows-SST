package com.example.baseproject.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.baseproject.model.local.data.GameEntity

const val GAMES_DB = "games_db"
const val GAMES_TABLE = "games"

@Database(
    entities = [GameEntity::class],
    version = 1
)

abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}
