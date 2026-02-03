import com.l13devstudio.buildSrc.PropertiesController

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.gms)
    alias(libs.plugins.cas)
}

android {
    namespace = "com.l13devstudio.app"
    compileSdk = 36

    flavorDimensions += "mods"

    productFlavors {
        PropertiesController.accessedMods().forEach { mod ->
            val config = PropertiesController.loadAddonConfig(mod)
            create(mod) {
                dimension = "mods"
                applicationId = config.packageName
                buildConfigField("String", "APPLICATION_ID", "\"${config.packageName}\"")
                buildConfigField("Boolean", "DARK_THEME", config.darkTheme)
                buildConfigField("Integer", "APP_ID", config.appId)
                buildConfigField("Integer", "PRIMARY_COLOR", config.appColor)
                buildConfigField("Integer", "PERCENT_SHOW_NATIVE_AD", config.percentNative)
                buildConfigField("Integer", "PERCENT_SHOW_INTER_AD", config.percentInter)

            }
        }
    }

    defaultConfig {

        val versionCode = property("versionCode")?.toString() ?: error("Not found versionCode properties")
        val versionName = property("versionName")?.toString() ?: error("Not found versionName properties")

        this.versionCode = versionCode.toInt()
        this.versionName = versionName

        minSdk = 24
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

cas {
    includeOptimalAds = true
    adapters {
        ironSource = true
        googleAds = true
        unityAds = true
        kidoz = true
        liftoffMonetize = true
        inMobi = true
        inmobi = true
        chartboost = true
        dtExchange = true
        mintegral = true
        appLovin = true
        applovin = true
        audienceNetwork = true
        pangle = true
        yangoAds = true
        bigoAds = true
        casExchange = true
        startIO = true
        hyprMX = true
        ysoNetwork = true
        ogury = true
        prado = true
        superAwesome = true
        verve = true
        smaato = true
        maticoo = true
    }
}

dependencies {

    implementation(libs.voyager.navigator)
    implementation(libs.play.services.ads)

    // Ads
    implementation (libs.mobileads.yandex)
//    implementation(libs.analytics)
    implementation(libs.picasso)

    // Koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Push Notifications
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.analytics)
//    implementation(libs.firebase.messaging)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":features:loader"))
    implementation(project(":features:dashboard"))
    implementation(project(":features:home"))
    implementation(project(":features:like"))
    implementation(project(":features:ad"))
    implementation(project(":features:faq"))
    implementation(project(":features:issue"))
    implementation(project(":features:suggest"))
    implementation(project(":features:guide"))
    implementation(project(":features:addon"))
    implementation(project(":features:download"))

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(project(":navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:android"))

}
