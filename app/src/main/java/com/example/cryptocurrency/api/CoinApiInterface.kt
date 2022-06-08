package com.example.cryptocurrency.api

import com.example.cryptocurrency.model.MarketModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CoinApiInterface {
    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    fun getMarketCoinData(): Call<MarketModel>
}