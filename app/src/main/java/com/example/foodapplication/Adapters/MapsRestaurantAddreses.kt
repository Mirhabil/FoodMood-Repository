package com.example.foodapplication.Adapters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyRestaurants(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): PlacesResponse
}

@Parcelize
data class PlacesResponse(
    val results: List<Place>,
    val status: String
):Parcelable

@Parcelize
data class Place(
    val name: String,
    val geometry: Geometry,
    val rating: Double?,
    val opening_hours: OpeningHours?,
    val photos: List<Photo>?
):Parcelable

@Parcelize
data class Photo(
    val photo_reference: String
):Parcelable

@Parcelize
data class OpeningHours(
    val open_now: Boolean?
):Parcelable

@Parcelize
data class Geometry(
    val location: LocationLatLng
):Parcelable

@Parcelize
data class LocationLatLng(
    val lat: Double,
    val lng: Double
):Parcelable