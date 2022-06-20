package com.example.cryptocurrency.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency.repository.MarketDataRepository

class MarketDataViewModelFactory(private val marketDataRepository: MarketDataRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarketDataViewModel(marketDataRepository) as T
    }

}