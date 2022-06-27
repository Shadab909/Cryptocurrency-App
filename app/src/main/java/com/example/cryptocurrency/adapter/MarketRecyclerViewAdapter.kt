package com.example.cryptocurrency.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.CurrencyItemLayoutBinding
import com.example.cryptocurrency.fragment.FavouritesFragmentDirections
import com.example.cryptocurrency.fragment.HomeFragmentDirections
import com.example.cryptocurrency.fragment.MarketFragmentDirections
import com.example.cryptocurrency.model.CryptoCurrency
import com.example.cryptocurrency.model.MarketModel


class MarketRecyclerViewAdapter(val destination : String) : ListAdapter<CryptoCurrency,MarketRecyclerViewAdapter.MarketViewHolder>(MarketDiffUtilCallback()) {

    inner class MarketViewHolder(binding: CurrencyItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val currencyImageView = binding.currencyImageView
        val currencyNameTextView = binding.currencyNameTextView
        val currencySymbolTextView = binding.currencySymbolTextView
        val currencyPriceTextView = binding.currencyPriceTextView
        val currencyChangeImageView = binding.currencyChangeImageView
        val currencyChangeTextView = binding.currencyChangeTextView
        val currencyChartImageView = binding.currencyChartImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CurrencyItemLayoutBinding.inflate(layoutInflater,parent,false)
        return MarketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = getItem(position)
        holder.currencyNameTextView.text = item.name
        holder.currencySymbolTextView.text = item.symbol
        holder.currencyPriceTextView.text = String.format("$%.02f",item.quotes[0].price)

        Glide.with(holder.currencyImageView.context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png")
            .thumbnail(Glide.with(holder.currencyImageView.context).load(R.drawable.spinner))
            .into(holder.currencyImageView)


        if (item.quotes[0].percentChange24h > 0){
            holder.currencyChangeTextView.setTextColor(holder.currencyChangeTextView.context.resources.getColor(R.color.green))
            holder.currencyChangeTextView.text = String.format("%.02f%%",item.quotes[0].percentChange24h)
            holder.currencyChangeImageView.setImageDrawable(holder.currencyChangeImageView.context.resources.getDrawable(R.drawable.ic_caret_up))
        }else{
            holder.currencyChangeTextView.setTextColor(holder.currencyChangeTextView.context.resources.getColor(R.color.red))
            holder.currencyChangeTextView.text = String.format("%.02f%%",item.quotes[0].percentChange24h)
            holder.currencyChangeImageView.setImageDrawable(holder.currencyChangeImageView.context.resources.getDrawable(R.drawable.ic_caret_down))
        }

        Glide.with(holder.currencyChartImageView.context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + item.id + ".png")
            .thumbnail(Glide.with(holder.currencyImageView.context).load(R.drawable.spinner))
            .into(holder.currencyChartImageView)

        holder.itemView.setOnClickListener {
            if (destination == "Market"){
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragment2ToDetailsFragment().setData(item)
                )
            }else if(destination == "Home"){
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragment2ToDetailsFragment().setData(item)
                )
            }else{
                findNavController(it).navigate(
                    FavouritesFragmentDirections.actionWatchlistFragment2ToDetailsFragment().setData(item)
                )
            }
        }


    }
}

class MarketDiffUtilCallback : DiffUtil.ItemCallback<CryptoCurrency>() {
    override fun areItemsTheSame(oldItem: CryptoCurrency, newItem: CryptoCurrency): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CryptoCurrency, newItem: CryptoCurrency): Boolean {
        return oldItem == newItem
    }
}