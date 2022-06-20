package com.example.cryptocurrency

import android.app.Application
import com.example.cryptocurrency.api.ApiInterface
import com.example.cryptocurrency.api.ApiUtilities
import com.example.cryptocurrency.repository.MarketDataRepository

class MyApplication : Application() {
    lateinit var marketDataRepository: MarketDataRepository
    override fun onCreate() {
        super.onCreate()

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        marketDataRepository = MarketDataRepository(apiInterface)
    }
}