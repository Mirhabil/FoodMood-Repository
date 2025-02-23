package com.example.foodapplication.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class RestaurantsModel(
    val posterPath:String?,
    val open:Boolean,
    val name:String
):Parcelable