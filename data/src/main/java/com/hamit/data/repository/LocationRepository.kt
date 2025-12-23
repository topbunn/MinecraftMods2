package com.hamit.data.repository

import android.content.Context
import android.telephony.TelephonyManager
import com.hamit.android.utils.AppLocation
import com.hamit.data.api.LocationApi
import kotlinx.coroutines.withTimeoutOrNull

class LocationRepository(
    private val context: Context,
    private val api: LocationApi,
) {

    suspend fun getLocation(): AppLocation {
        val country = getCountryFromIpWithTimeout()
        return country?.countryCodeToLocationAd() ?: getLocationFromTelephony(context)
    }

    private suspend fun getCountryFromIpWithTimeout() = withTimeoutOrNull(3000) {
        try {
            val response = api.getInfo()
            response.countryCode?.lowercase()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun getLocationFromTelephony(context: Context): AppLocation {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountry = telephonyManager.simCountryIso?.lowercase()
        val networkCountry = telephonyManager.networkCountryIso?.lowercase()
        val localeCountry = context.resources.configuration.locales[0]?.country?.lowercase()

        val country = when {
            !simCountry.isNullOrBlank() -> simCountry
            !networkCountry.isNullOrBlank() -> networkCountry
            !localeCountry.isNullOrBlank() -> localeCountry
            else -> "other"
        }

        return country.countryCodeToLocationAd()
    }

    private fun String?.countryCodeToLocationAd() =
        if (this == "ru") AppLocation.RU else AppLocation.OTHER
}
