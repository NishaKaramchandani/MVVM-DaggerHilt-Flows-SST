package com.example.baseproject.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.baseproject.model.local.GamesLocalDataSource
import com.example.baseproject.model.local.GamesDao
import com.example.baseproject.model.local.GAMES_DB
import com.example.baseproject.model.local.GamesDatabase
import com.example.baseproject.model.remote.ApiService
import com.example.baseproject.model.remote.GamesRemoteDataSource
import com.example.baseproject.model.repository.GamesRepository
import com.example.baseproject.model.repository.GamesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@HiltAndroidApp
class BaseApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun createDatabaseClient(@ApplicationContext appContext: Context): GamesDatabase =
        Room.databaseBuilder(
            appContext,
            GamesDatabase::class.java,
            GAMES_DB
        ).build()

    @Provides
    @Singleton
    fun createGamesDaoClient(database: GamesDatabase): GamesDao = database.gamesDao()

    @Provides
    @Singleton
    fun provideGamesLocalDataSource(gamesDao: GamesDao): GamesLocalDataSource =
        GamesLocalDataSource(gamesDao)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): GamesRemoteDataSource =
        GamesRemoteDataSource(apiService = apiService)

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRepository(
        gamesLocalDataSource: GamesLocalDataSource,
        remoteDataSource: GamesRemoteDataSource
    ): GamesRepository =
        GamesRepositoryImpl(gamesLocalDataSource, remoteDataSource)
}


