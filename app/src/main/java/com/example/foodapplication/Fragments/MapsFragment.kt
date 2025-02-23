package com.example.foodapplication.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.foodapplication.Models.CategoriesModel
import com.example.foodapplication.GeneralSettings
import com.example.foodapplication.Adapters.PlacesApiService
import com.example.foodapplication.R
import com.example.foodapplication.Models.RestaurantsModel
import com.example.foodapplication.Restaurants_Details_Bottom_Sheet
import com.example.foodapplication.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val API_KEY = "AIzaSyDRmaZzjW3FivCGmAsQXoM20WSdo6gyAsQ"
    private val BAKU_LAT_LNG = "40.4093,49.8671"

    private lateinit var binding: FragmentMapsBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        setupMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }

    private fun setupMap() {

        fetchNearbyRestaurants()

        map.uiSettings.isZoomControlsEnabled = true

        // Enable location tracking
        enableMyLocation()

        // Set up marker drag events
        markerDragFun(map)

        // Set custom map style
        setMapStyle(map)
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        map.isMyLocationEnabled = true

        // Get last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLocation = LatLng(it.latitude, it.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                map.addMarker(MarkerOptions().position(userLocation).title("You are here"))
            }
        }
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val successLoadStyle = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), GeneralSettings.mapStyle)
            )
            if (!successLoadStyle) {
                Log.d("Maps", "Failed to add Style")
            }
        } catch (e: Exception) {
            Log.d("Maps", e.toString())
        }
    }

    private fun markerDragFun(googleMap: GoogleMap) {
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {
                Toast.makeText(requireContext(), "OnMarkerDrag", Toast.LENGTH_SHORT).show()
            }

            override fun onMarkerDragEnd(marker: Marker) {
                Toast.makeText(requireContext(), "OnMarkerDragEnd", Toast.LENGTH_SHORT).show()
            }

            override fun onMarkerDragStart(marker: Marker) {
                Toast.makeText(requireContext(), "OnMarkerDragStart", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun fetchNearbyRestaurants() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PlacesApiService::class.java)

        val listData = mutableListOf<RestaurantsModel>()

        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    service.getNearbyRestaurants(BAKU_LAT_LNG, 10000, "restaurant", API_KEY)
                }

                val bundle = Bundle()
                bundle.putParcelable("restaurant", response)


                val restaurantsDetailsBottomSheet = Restaurants_Details_Bottom_Sheet()
                restaurantsDetailsBottomSheet.arguments = bundle

                binding.btnBottomSheet.setOnClickListener {
                    restaurantsDetailsBottomSheet.show(parentFragmentManager, restaurantsDetailsBottomSheet.tag)
                }

//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragmentContainerView, restaurantsDetailsBottomSheet)
//                    .addToBackStack(null) // optional, if you want to add to the back stack
//                    .commit()


                response.results.forEach { place ->
                    val latLng = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                    map.addMarker(MarkerOptions().position(latLng).title(place.name))

                    listData += RestaurantsModel(
                        posterPath = place.photos?.firstOrNull()?.photo_reference?.let { ref ->
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$ref&key=$API_KEY"
                        } ?: "",
                        name = place.name,
                        open = place.opening_hours?.open_now ?: false
                    )
                }

//                val adapterRestaurants = RestaurantsAdapter()
//                adapterRestaurants.getAdapterList(listData)
//                binding.recyclerView.adapter = adapterRestaurants

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching restaurants: ${e.message}")
            }
        }
    }
}

val list: List<RestaurantsModel> = listOf<RestaurantsModel>(
    RestaurantsModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true
    ),
    RestaurantsModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true
    ),
    RestaurantsModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true
    ),
    RestaurantsModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true
    ),
    RestaurantsModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true
    ),
    RestaurantsModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true
    )
)

val list2: List<CategoriesModel> = listOf<CategoriesModel>(
    CategoriesModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269
    ),
    CategoriesModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269
    ),
    CategoriesModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269
    ),
    CategoriesModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269
    ),
    CategoriesModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269
    ),
    CategoriesModel(
        posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269
    )
)



