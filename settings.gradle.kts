pluginManagement {
    repositories {
        google()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven")
        maven("https://artifact.bytedance.com/repository/pangle")
        maven {
            name = "MintegralAdsRepo"
            url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
            content { includeGroup("com.mbridge.msdk.oversea") }
        }
        maven {
            name = "PangleAdsRepo"
            url = uri("https://artifact.bytedance.com/repository/pangle")
            content { includeGroup("com.pangle.global") }
        }
        maven {
            name = "ChartboostAdsRepo"
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-ads/")
            content {
                includeGroup("com.chartboost")
                includeGroup("com.iab.omid.library")
            }
        }
        maven {
            name = "YSONetworkRepo"
            url = uri("https://ysonetwork.s3.eu-west-3.amazonaws.com/sdk/android")
            content { includeGroup("com.ysocorp") }
        }
        maven {
            name = "OguryAdsRepo"
            url = uri("https://maven.ogury.co")
            content {
                includeGroup("co.ogury")
                includeGroup("co.ogury.module")
            }
        }
        maven {
            name = "SmaatoAdsRepo"
            url = uri("https://s3.amazonaws.com/smaato-sdk-releases/")
            content { includeGroup("com.smaato.android.sdk") }
        }
        maven {
            name = "VerveAdsRepo"
            url = uri("https://verve.jfrog.io/artifactory/verve-gradle-release")
            content {
                includeGroup("net.pubnative")
                includeGroup("com.verve")
            }
        }
    }
}



rootProject.name = "Minecraft Mods"
include(":app")
include(":domain")
include(":core")
include(":core:ui")
include(":core:android")
include(":data")
include(":features")
include(":navigation")
include(":features:home")
include(":features:loader")
include(":features:addon")
include(":features:like")
include(":features:guide")
include(":features:dashboard")
include(":features:suggest")
include(":features:download")
include(":features:ad")
include(":features:faq")
include(":features:issue")
