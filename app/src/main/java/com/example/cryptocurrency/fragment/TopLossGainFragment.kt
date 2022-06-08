package com.example.cryptocurrency.fragment

import android.os.Bundle
import android.util.Log
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
import com.example.cryptocurrency.api.CoinService
import com.example.cryptocurrency.databinding.FragmentTopLossGainBinding
import com.example.cryptocurrency.model.CryptoCurrency
import com.example.cryptocurrency.model.MarketModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*
import kotlin.collections.ArrayList


class TopLossGainFragment : Fragment() {

    private lateinit var binding : FragmentTopLossGainBinding
    private lateinit var mAdapter : MarketRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_top_loss_gain, container, false)
        mAdapter = MarketRecyclerViewAdapter("Home")
        binding.topGainLoseRecyclerView.adapter = mAdapter
        getMarketCoinDataWithSingleton()

        return binding.root
    }

    private fun getMarketData() {
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null){

                withContext(Dispatchers.Main){
                    val position = requireArguments().getInt("position")
                    val dataItem = res.body()!!.data.cryptoCurrencyList
                    Collections.sort(dataItem){
                            item1 , item2 -> (item2.quotes[0].percentChange24h.toInt())
                        .compareTo(item1.quotes[0].percentChange24h.toInt())
                    }

                    val list = ArrayList<CryptoCurrency>()


                    if (position == 0){
                        list.clear()
                        for ( i in 0..19){
                            list.add(dataItem[i])
                        }
                        mAdapter.submitList(list)
                        binding.spinKitView.visibility = GONE
                    }else{
                        list.clear()
                        for ( i in 0..19){
                            list.add(dataItem[dataItem.size - 1 - i])
                        }
                        mAdapter.submitList(list)
                        binding.spinKitView.visibility = GONE
                    }
                }
            }

        }
    }

    private fun getMarketCoinDataWithSingleton(){

            val coins = CoinService.apiInstance.getMarketCoinData()


                coins.enqueue(object : Callback<MarketModel>{
                    override fun onResponse(call: Call<MarketModel>, response: Response<MarketModel>) {
                        Log.d("TopLossGainFragment", "Success")
                        val res = response
                        if (res.body() != null){
                            val position = requireArguments().getInt("position")
                            val dataItem = res.body()!!.data.cryptoCurrencyList
                            Collections.sort(dataItem){
                                    item1 , item2 -> (item2.quotes[0].percentChange24h.toInt())
                                .compareTo(item1.quotes[0].percentChange24h.toInt())
                            }

                            val list = ArrayList<CryptoCurrency>()


                            if (position == 0){
                                list.clear()
                                for ( i in 0..19){
                                    list.add(dataItem[i])
                                }
                                mAdapter.submitList(list)
                                binding.spinKitView.visibility = GONE
                            }else{
                                list.clear()
                                for ( i in 0..19){
                                    list.add(dataItem[dataItem.size - 1 - i])
                                }
                                mAdapter.submitList(list)
                                binding.spinKitView.visibility = GONE
                            }
                        }
                    }

                    override fun onFailure(call: Call<MarketModel>, t: Throwable) {
                        Log.d("TopLossGainFragment", t.toString())
                    }
                })


    }


    override fun onResume() {
        super.onResume()
        binding.topGainLoseRecyclerView.adapter = mAdapter
    }


}