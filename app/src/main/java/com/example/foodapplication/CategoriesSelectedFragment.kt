package com.example.foodapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapplication.databinding.FragmentCategoriesSelectedBinding

class CategoriesSelectedFragment : Fragment() {

    private lateinit var binding:FragmentCategoriesSelectedBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentCategoriesSelectedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter=RestaurantsAdapter()
        binding.rv.adapter=adapter
        adapter.getAdapterList(list)

        val categoriesAdapter=CategoriesAdapter()
        binding.recyclerView2.adapter=categoriesAdapter
        categoriesAdapter.getAdapterList(list2)
    }


}