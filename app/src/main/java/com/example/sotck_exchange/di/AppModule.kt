package com.example.stock_exchange.di


import com.example.sotck_exchange.domain.IStockRepository
import com.example.sotck_exchange.network.StockRepository
import com.example.stock_exchange.network.SocketCommunicator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideSocketCommunicator(): SocketCommunicator {
        return SocketCommunicator()
    }

    @Provides
    @Singleton
    fun provideStockRepository(socketCommunicator: SocketCommunicator): IStockRepository {
        return StockRepository(socketCommunicator)
    }

}