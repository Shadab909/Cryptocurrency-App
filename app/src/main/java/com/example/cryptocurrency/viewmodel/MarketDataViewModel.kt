package com.example.cryptocurrency.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.model.MarketModel
import com.example.cryptocurrency.repository.MarketDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarketDataViewModel(private val marketDataRepository: MarketDataRepository) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            marketDataRepository.getCrypto()
        }
    }

    val cryptoData : LiveData<MarketModel>
    get() = marketDataRepository._marketDataLiveData
}