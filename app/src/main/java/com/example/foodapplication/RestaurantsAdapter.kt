package com.example.foodapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapplication.databinding.RestaurantsItemBinding

class RestaurantsAdapter : RecyclerView.Adapter<RestaurantsViewHolder>() {
    var adapterList: List<RestaurantsModel> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        return RestaurantsViewHolder(
            binding = RestaurantsItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        val binding = holder.binding
        val items = adapterList[position]

        Glide.with(binding.posterPath).load(items.posterPath).into(binding.posterPath)
        binding.tvName.text = items.name
        if (items.open) {
            binding.tvOpen.text = "Open"
        } else {
            binding.tvOpen.text = "Close"
        }
    }

    fun getAdapterList(newAdapterList:List<RestaurantsModel>){

        adapterList=newAdapterList
        notifyDataSetChanged()
    }
}

class RestaurantsViewHolder(val binding:RestaurantsItemBinding) :
    RecyclerView.ViewHolder(binding.root)