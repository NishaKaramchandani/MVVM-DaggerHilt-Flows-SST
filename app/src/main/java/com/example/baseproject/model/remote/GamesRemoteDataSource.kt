package com.example.baseproject.model.remote

import com.example.baseproject.model.remote.data.GameResponse
import okhttp3.ResponseBody
import java.io.IOException
import javax.inject.Inject

/**
 * Remote data source class to fetch data from API
 */
class GamesRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchGames(): Result<List<GameResponse>> {
        val gamesResponse = apiService.getAllGamesList()
        if (gamesResponse.isSuccessful) {
            return (gamesResponse.body()?.let { Result.success(it) }
                ?: Result.failure(IOException("No data received.")))
        }
        val error: ResponseBody? = gamesResponse.errorBody()
        return Result.failure(IOException(error.toString()))
    }

}