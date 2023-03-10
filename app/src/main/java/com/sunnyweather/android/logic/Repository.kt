package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        emit(
            try {
                val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
                if (placeResponse.status == "ok") {
                    Result.success(placeResponse.places)
                } else {
                    Result.failure(RuntimeException("response status is ${placeResponse.status}"))
                }
            } catch (e: Exception) {
                Result.failure(RuntimeException(e))
            }
        )
    }
}