package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.PlaceResponse
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException

object Repository {
    fun searchPlaces(query: String) = fire {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            Result.success(placeResponse.places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun savePlace(place: PlaceResponse.Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavePlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
    fun refreshWeather(lng: String, lat: String) = fire {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                Result.success(
                    Weather(
                        realtimeResponse.result.realtime,
                        dailyResponse.result.daily
                    )
                )
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(block: suspend () -> Result<T>) =
        liveData {
            emit(
                try {
                    block()
                } catch (e: Exception) {
                    Result.failure(e)
                }
            )
        }
}