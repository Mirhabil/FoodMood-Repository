package com.example.foodapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.foodapplication.Adapters.PlacesResponse
import com.example.foodapplication.Adapters.RestaurantsAdapter
import com.example.foodapplication.Models.RestaurantsModel
import com.example.foodapplication.databinding.FragmentRestaurantsDetailsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Restaurants_Details_Bottom_Sheet : BottomSheetDialogFragment() {

    private val API_KEY = "AIzaSyDRmaZzjW3FivCGmAsQXoM20WSdo6gyAsQ"

    private lateinit var binding:FragmentRestaurantsDetailsBottomSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentRestaurantsDetailsBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter= RestaurantsAdapter(onClick = { findNavController().navigate(Restaurants_Details_Bottom_SheetDirections.actionRestaurantsDetailsBottomSheetToMapsFragment()) })
        binding.rv.adapter=adapter
        val response= arguments?.getParcelable<PlacesResponse>("restaurant")


        if (response != null && response.results.isNotEmpty()) {
            val listRestaurants = mutableListOf<RestaurantsModel>()
            response.results.forEach { place ->
                listRestaurants += RestaurantsModel(
                    posterPath = place.photos?.firstOrNull()?.photo_reference?.let { ref ->
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$ref&key=$API_KEY"
                    } ?: "",
                    name = place.name,
                    open = place.opening_hours?.open_now ?: false
                )
            }

            adapter.getAdapterList(listRestaurants)
        } else {
            // Handle the case when no data is available
            Toast.makeText(requireContext(), "No restaurant data available", Toast.LENGTH_SHORT).show()
        }

    }


}