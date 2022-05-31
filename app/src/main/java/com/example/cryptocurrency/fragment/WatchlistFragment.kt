package com.example.cryptocurrency.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.cryptocurrency.R
import com.example.cryptocurrency.adapter.MarketRecyclerViewAdapter
import com.example.cryptocurrency.api.ApiInterface
import com.example.cryptocurrency.api.ApiUtilities
import com.example.cryptocurrency.databinding.FragmentWatchlistBinding
import com.example.cryptocurrency.model.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.create

class WatchlistFragment : Fragment() {

    private lateinit var binding : FragmentWatchlistBinding
    private lateinit var watchlist : ArrayList<String>
    private lateinit var watchlistItems : ArrayList<CryptoCurrency>
    private lateinit var mAdapter : MarketRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_watchlist, container, false)

        readData()

        lifecycleScope.launch(Dispatchers.IO)
        {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null){
                withContext(Dispatchers.Main){
                    watchlistItems = ArrayList()
                    watchlistItems.clear()

                    for (watchData in watchlist){
                        for (item in res.body()!!.data.cryptoCurrencyList){
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