package com.wipro.facts.di

import android.app.Application
import androidx.room.Room
import com.wipro.facts.api.FactsAPI
import com.wipro.facts.data.FactsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(FactsAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCarListAPI(retrofit: Retrofit): FactsAPI =
        retrofit.create(FactsAPI::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): FactsDatabase =
        Room.databaseBuilder(app, FactsDatabase::class.java, "facts_database")
            .build()
}