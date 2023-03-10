package com.sunnyweather.android.ui.place

import androidx.lifecycle.*
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.PlaceResponse

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<PlaceResponse.Place>()

    fun savePlace(place: PlaceResponse.Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavedPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()

    val placeLiveData = searchLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}