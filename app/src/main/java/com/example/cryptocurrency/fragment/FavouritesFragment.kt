package com.example.cryptocurrency.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cryptocurrency.R
import com.example.cryptocurrency.adapter.MarketRecyclerViewAdapter
import com.example.cryptocurrency.api.ApiInterface
import com.example.cryptocurrency.api.ApiUtilities
import com.example.cryptocurrency.databinding.FragmentFavouritesBinding
import com.example.cryptocurrency.model.CryptoCurrency
import com.example.cryptocurrency.repository.MarketDataRepository
import com.example.cryptocurrency.viewmodel.MarketDataViewModel
import com.example.cryptocurrency.viewmodel.MarketDataViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesFragment : Fragment() {

    private lateinit var binding : FragmentFavouritesBinding
    private lateinit var watchlist : ArrayList<String>
    private lateinit var watchlistItems : ArrayList<CryptoCurrency>
    private lateinit var mainList : List<CryptoCurrency>
    private lateinit var mAdapter : MarketRecyclerViewAdapter
    private lateinit var viewModel : MarketDataViewModel
    private lateinit var marketDataRepository: MarketDataRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favourites, container, false)

        readData()
        mainList = listOf()

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        marketDataRepository = MarketDataRepository(apiInterface)
        viewModel = ViewModelProvider(this, MarketDataViewModelFactory(marketDataRepository))[MarketDataViewModel::class.java]
        viewModel.cryptoData.observe(viewLifecycleOwner){
            mainList = it.data.cryptoCurrencyList
        }


        lifecycleScope.launch(Dispatchers.IO)
        {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null){
                withContext(Dispatchers.Main){
                    watchlistItems = ArrayList()
                    watchlistItems.clear()

//                    for (watchData in watchlist){
//                        for (item in res.body()!!.data.cryptoCurrencyList){
//                            if (watchData == item.symbol){
//                                watchlistItems.add(item)
//                            }
//                        }
//                    }
                    for (watchData in watchlist){
                        for (item in mainList){
                            if (watchData == item.symbol){
                                watchlistItems.add(item)
                            }
                        }
                    }
                    mAdapter = MarketRecyclerViewAdapter("Watchlist")
                    binding.watchlistCurrencyRecyclerView.adapter = mAdapter
                    binding.watchlistSpinKitView.visibility = GONE
                    mAdapter.submitList(watchlistItems)
                }
            }


        }
        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist",ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json,type)
    }
}