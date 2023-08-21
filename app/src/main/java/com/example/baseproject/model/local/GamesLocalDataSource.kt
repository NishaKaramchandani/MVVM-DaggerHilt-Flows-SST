package com.example.baseproject.model.local

import com.example.baseproject.model.local.data.GameEntity
import javax.inject.Inject

/**
 * Local data source class to fetch data from database
 */
class GamesLocalDataSource @Inject constructor(private val gamesDao: GamesDao) {

    fun insertGame(gameEntity: GameEntity) {
        gamesDao.insertGameEntity(gameEntity)
    }

    fun insertAllGames(gameEntities: List<GameEntity>) {
        gamesDao.insertAll(gameEntities)
    }

    fun getAllGames() = gamesDao.getAllGames()

}