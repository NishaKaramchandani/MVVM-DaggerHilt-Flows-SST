package com.example.baseproject.model.remote

import com.example.baseproject.model.remote.data.GameResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * API service class
 */
interface ApiService {

    companion object {
        const val BASE_URL = "https://s.yimg.com/"
    }
    @GET("cv/ae/default/171221/soccer_game_results.json")
    suspend fun getAllGamesList(): Response<List<GameResponse>>
}