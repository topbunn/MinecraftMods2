package ru.topbun.buildSrc

import java.io.File
import java.util.*

object PropertiesController {

    private val configDir = File("properties")

    fun accessedMods(): List<String> =
        configDir.listFiles { f -> f.extension == "properties" }
            ?.map { it.nameWithoutExtension }
            ?.sorted()
            ?: emptyList()

    fun loadAddonConfig(modName: String): AddonConfig {
        val file = configDir.resolve("$modName.properties")
        require(file.exists()) { "Properties file for mod '$modName' not found at ${file.path}" }
        val props = Properties().apply { load(file.inputStream()) }

        val gradlePropertiesFile = File("gradle.properties")
        val gradleProperties = Properties().apply { load(gradlePropertiesFile.inputStream()) }

        fun getOrThrow(props: Properties, key: String) =
            props.getProperty(key) ?: throw IllegalArgumentException("Missing '$key' in $modName.properties")


        return AddonConfig(
            applovinSdkKey = getOrThrow(props, "applovin_sdk_key"),
            yandexMetricKey = getOrThrow(props, "metric_key"),
            packageName = getOrThrow(props, "applicationId"),
            appId = getOrThrow(props, "app_id"),
            appColor = getOrThrow(props, "primary_color"),
            percentNative = getOrThrow(props, "percent_show_native_ad"),
            percentInter = getOrThrow(props, "percent_show_inter_ad"),
            ruStore = getOrThrow(gradleProperties, "rustore")
        )
    }
}