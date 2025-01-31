package com.example.foodapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapplication.databinding.CategoriesItemBinding
import com.example.foodapplication.databinding.RestaurantsItemBinding

class CategoriesAdapter:RecyclerView.Adapter<CategoriesViewHolder>() {

    var adapterList: List<CategoriesModel> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            binding = CategoriesItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val binding = holder.binding
        val items = adapterList[position]

        Glide.with(binding.categoriesPosterPath).load(items.posterPath).into(binding.categoriesPosterPath)
        binding.tvCategoryName.text = items.name
        binding.tvCategoryPlaceCount.text="${items.placeCount} Places"
    }

    fun getAdapterList(newAdapterList:List<CategoriesModel>){

        adapterList=newAdapterList
        notifyDataSetChanged()
    }
}

class CategoriesViewHolder(val binding:CategoriesItemBinding) :
    RecyclerView.ViewHolder(binding.root)