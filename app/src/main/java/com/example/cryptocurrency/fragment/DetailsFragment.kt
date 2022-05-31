package com.example.cryptocurrency.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentDetailsBinding

import com.example.cryptocurrency.model.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val args : DetailsFragmentArgs by navArgs()
    private var watchlist : ArrayList<String>? = null
    private var isWatchlistChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details, container, false)

        val data = args.data!!

        binding.backStackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        setUpDetails(data)
        loadChart(data)
        setUpButtonOnClickListener(data)
        addDataToWatchlist(data)
        return binding.root
    }

    private fun addDataToWatchlist(data: CryptoCurrency) {
        readData()
        isWatchlistChecked = if (watchlist!!.contains(data.symbol)){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        }else{
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }

        binding.addWatchlistButton.setOnClickListener {
            isWatchlistChecked =
                if (!isWatchlistChecked){
                    if (!watchlist!!.contains(data.symbol)){
                        watchlist!!.add(data.symbol)
                    }
                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                    true
                }else{
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchlist!!.remove(data.symbol)
                    storeData()
                    false
                }
        }
    }

    private fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchlist)
        editor.putString("watchlist",json)
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist",Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist",ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json,type)
    }

    private fun setUpButtonOnClickListener(data: CryptoCurrency) {
        val oneMonth = binding.oneMonthBtn
        val fifteenMin = binding.fifteenMinBtn
        val oneHour = binding.oneHrBtn
        val fourHour = binding.fourHrBtn
        val oneWeek = binding.oneWeekBtn
        val oneDay = binding.oneDayBtn

        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMin.id -> loadChartWithTime(it,"15",data,oneMonth,oneHour,fourHour,oneWeek,oneDay)
                oneMonth.id -> loadChartWithTime(it,"M",data,fifteenMin,oneHour,fourHour,oneWeek,oneDay)
                oneHour.id -> loadChartWithTime(it,"1H",data,oneMonth,fifteenMin,fourHour,oneWeek,oneDay)
                fourHour.id -> loadChartWithTime(it,"4H",data,oneMonth,oneHour,fifteenMin,oneWeek,oneDay)
                oneWeek.id -> loadChartWithTime(it,"W",data,oneMonth,oneHour,fourHour,fifteenMin,oneDay)
                oneDay.id -> loadChartWithTime(it,"D",data,oneMonth,oneHour,fourHour,oneWeek,fifteenMin)

            }
        }
        oneMonth.setOnClickListener(clickListener)
        fifteenMin.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
    }

    private fun loadChartWithTime(
        it: View?,
        s: String,
        data: CryptoCurrency,
        btn1: AppCompatButton,
        btn2: AppCompatButton,
        btn3: AppCompatButton,
        btn4: AppCompatButton,
        btn5: AppCompatButton
    ) {
        disableButtons(btn1,btn2,btn3,btn4,btn5)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl(
            "https://www.tradingview.com/widgetembed/? frameElementId=tradingview_76d87&symbol=" + data.symbol
                    + "USD&interval=" + s + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3 F6&stu" +
                    "dies=1&hideideas=1&theme=Dark&style=1&timezone=Etc %2FUTC&studies_overrides={ }&overrides={}&enabled_features: []&disabled" +
                    "_features: []&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=cha+rt"
        )
    }

    private fun disableButtons(btn1: AppCompatButton, btn2: AppCompatButton, btn3: AppCompatButton, btn4: AppCompatButton, btn5: AppCompatButton) {
        btn1.background = null
        btn2.background = null
        btn3.background = null
        btn4.background = null
        btn5.background = null
    }

    private fun loadChart(data: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl(
            "https://www.tradingview.com/widgetembed/? frameElementId=tradingview_76d87&symbol=" + data.symbol
                    + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3 F6&stu" +
                    "dies=1&hideideas=1&theme=Dark&style=1&timezone=Etc %2FUTC&studies_overrides={ }&overrides={}&enabled_features: []&disabled" +
                    "_features: []&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=cha+rt"
        )
    }

    private fun setUpDetails(data: CryptoCurrency) {
        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id + ".png")
            .thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .circleCrop()
            .into(binding.detailImageView)

        binding.detailSymbolTextView.text = data.symbol
        binding.detailPriceTextView.text = String.format("$%.02f",data.quotes[0].price)

        if (data.quotes[0].percentChange24h > 0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeTextView.text = String.format("%.02f",data.quotes[0].percentChange24h)
            binding.detailChangeImageView.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_caret_up))
        }else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeTextView.text = String.format("%.02f",data.quotes[0].percentChange24h)
            binding.detailChangeImageView.setImageDrawable(requireContext().resources.getDrawable(R.drawable.ic_caret_down))
        }
    }


}