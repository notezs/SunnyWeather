package com.sunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork

object PlaceDao {
    fun savePlace(place: PlaceResponse.Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavePlace(): PlaceResponse.Place {
        return Gson().fromJson(sharedPreferences().getString("place", ""), PlaceResponse.Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")
    private fun sharedPreferences() =
        SunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)
}