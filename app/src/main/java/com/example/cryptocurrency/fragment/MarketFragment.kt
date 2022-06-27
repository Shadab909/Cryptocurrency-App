package com.example.cryptocurrency.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cryptocurrency.MyApplication
import com.example.cryptocurrency.R
import com.example.cryptocurrency.adapter.MarketRecyclerViewAdapter
import com.example.cryptocurrency.api.ApiInterface
import com.example.cryptocurrency.api.ApiUtilities
import com.example.cryptocurrency.databinding.FragmentMarketBinding
import com.example.cryptocurrency.model.CryptoCurrency
import com.example.cryptocurrency.repository.MarketDataRepository
import com.example.cryptocurrency.viewmodel.MarketDataViewModel
import com.example.cryptocurrency.viewmodel.MarketDataViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.create
import java.util.*
import kotlin.collections.ArrayList


class MarketFragment : Fragment() {

    private lateinit var binding : FragmentMarketBinding
    private lateinit var mAdapter : MarketRecyclerViewAdapter
    private lateinit var list : List<CryptoCurrency>
    private lateinit var searchText : String
    private lateinit var viewModel : MarketDataViewModel
    private lateinit var marketDataRepository: MarketDataRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_market, container, false)
        mAdapter = MarketRecyclerViewAdapter("Market")
        list = listOf()
        searchText = ""

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        marketDataRepository = MarketDataRepository(apiInterface)
        viewModel = ViewModelProvider(this, MarketDataViewModelFactory(marketDataRepository))[MarketDataViewModel::class.java]

        binding.currencyRecyclerView.adapter = mAdapter

//        lifecycleScope.launch(Dispatchers.IO){
//            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
//
//            withContext(Dispatchers.Main){
//                if (res.body() != null){
//                    list = res.body()!!.data.cryptoCurrencyList
//                    mAdapter.submitList(list)
//                    binding.spinKitView.visibility = GONE
//                }
//            }
//        }
        viewModel.cryptoData.observe(viewLifecycleOwner){
            list = it.data.cryptoCurrencyList
            mAdapter.submitList(list)
            binding.spinKitView.visibility = GONE
        }


        coinSearchEditText()

        return binding.root
    }

    private fun coinSearchEditText() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().lowercase(Locale.getDefault())
                updateRecyclerViewWithSearchText()
            }

        })
    }

    private fun updateRecyclerViewWithSearchText() {
        val data = ArrayList<CryptoCurrency>()

        for (item in list){
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbol = item.symbol.lowercase(Locale.getDefault())

            if (coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }
        }

        mAdapter.submitList(data)
    }

}