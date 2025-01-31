package com.example.foodapplication

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

class MapsFragment : Fragment() {

    private lateinit var binding:FragmentMapsBinding

    private lateinit var map: GoogleMap
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
        binding=FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        val adapterRestaurants=RestaurantsAdapter()
        binding.recyclerView.adapter=adapterRestaurants
        adapterRestaurants.getAdapterList(list)


    }

    private fun setupMap() {
        val sydney = LatLng(-34.0, 151.0)
        val restaurants = listOf(
            LatLng(40.3765, 49.8470), // Firuze Restaurant
            LatLng(40.3709, 49.8408), // Nargiz Restaurant
            LatLng(40.3753, 49.8482), // Dolma Restaurant
            LatLng(40.3742, 49.8461), // Sumakh Restaurant
            LatLng(40.3725, 49.8469), // Chayki Restaurant
            LatLng(40.3747, 49.8438), // Art Club Restaurant
            LatLng(40.3703, 49.8391), // Zafferano Restaurant
            LatLng(40.3720, 49.8487), // Sahil Bar & Restaurant
            LatLng(40.3728, 49.8382), // 7 Gözəl Restaurant
            LatLng(40.3758, 49.8463)  // Qaynana Restaurant
        )

        restaurants.forEach {
            map.addMarker(MarkerOptions().position(it).title("Marker in Sydney").draggable(true))
        }


        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

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
}

val list:List<RestaurantsModel> = listOf<RestaurantsModel>(
    RestaurantsModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true),
    RestaurantsModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true),
    RestaurantsModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true),
    RestaurantsModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true),
    RestaurantsModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true),
    RestaurantsModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "By Ruslan Pizza - 28 May", open = true)
)

val list2:List<CategoriesModel> = listOf<CategoriesModel>(
    CategoriesModel(posterPath =  "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269),
    CategoriesModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269),
    CategoriesModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269),
    CategoriesModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269),
    CategoriesModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269),
    CategoriesModel(posterPath = "https://lh5.googleusercontent.com/p/AF1QipNJJHvsIpYDyCAl5KMcemAvbOeqnerTqc5ktK4-=w408-h285-k-no",
        name = "Dessert", placeCount = 269)
)


