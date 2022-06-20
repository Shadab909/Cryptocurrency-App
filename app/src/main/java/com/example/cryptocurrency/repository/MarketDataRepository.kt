package com.example.cryptocurrency.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrency.api.ApiInterface
import com.example.cryptocurrency.model.MarketModel

class MarketDataRepository(private val apiInterface: ApiInterface) {
    private val marketDataLiveData = MutableLiveData<MarketModel>()

    val _marketDataLiveData : LiveData<MarketModel>
    get() = marketDataLiveData

    suspend fun getCrypto(){
        val result = apiInterface.getMarketData()
        if (result.body() != null){
            marketDataLiveData.postValue(result.body())
        }
    }

}