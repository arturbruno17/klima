package com.posart.klima.data.remote.di

import com.posart.klima.data.remote.WeatherService
import com.posart.klima.data.remote.WeatherServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
abstract class DataRemoteBindsModule {
    @Binds
    abstract fun bindWeatherService(
        weatherServiceImpl: WeatherServiceImpl
    ): WeatherService
}


@Module
@InstallIn(SingletonComponent::class)
object DataRemoteProvidesModule {
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}