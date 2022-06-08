package com.example.cryptocurrency.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinService {
    val apiInstance : CoinApiInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coinmarketcap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInstance = retrofit.create(CoinApiInterface::class.java)
    }
}