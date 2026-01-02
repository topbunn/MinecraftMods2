package com.hamit.data.repository

import android.content.Context
import android.telephony.TelephonyManager
import com.hamit.data.source.remote.api.RegionApi
import com.hamit.data.source.remote.dto.region.RegionDto
import com.hamit.domain.entity.AppLocation
import com.hamit.domain.repository.RegionRepository
import io.ktor.client.call.body
import kotlinx.coroutines.withTimeoutOrNull

class RegionRepositoryImpl(
    private val context: Context,
    private val regionApi: RegionApi,
) : RegionRepository {

    override suspend fun receiveRegion(): AppLocation {
        val region = getZoneFromNetworkWithTimeout()
        return region?.zoneCodeToRegionType() ?: getRegionFromTelephony(context)
    }

    private suspend fun getZoneFromNetworkWithTimeout() = withTimeoutOrNull(3000) {
        try {
            val response = regionApi.fetchRegionData().body<RegionDto>()
            response.zoneCode?.lowercase()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun getRegionFromTelephony(context: Context): AppLocation {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountry = telephonyManager.simCountryIso?.lowercase()
        val networkCountry = telephonyManager.networkCountryIso?.lowercase()
        val localeCountry = context.resources.configuration.locales[0]?.country?.lowercase()

        val country = when {
            !simCountry.isNullOrBlank() -> simCountry
            !networkCountry.isNullOrBlank() -> networkCountry
            !localeCountry.isNullOrBlank() -> localeCountry
            else -> "other"
        }

        return country.zoneCodeToRegionType()
    }

    private fun String?.zoneCodeToRegionType() =
        if (this == "ru") AppLocation.RU else AppLocation.OTHER
}
