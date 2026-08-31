package com.kito.feature.home.presentation.components

import com.kito.feature.app.presentation.isAndroid


private const val KHAOO_GULLY_ANDROID_LINK = "https://play.google.com/store/apps/details?id=com.khaogully.app"

private const val KHAOO_GULLY_IOS_LINK = "https://apps.apple.com/in/app/khaoogully/id6789093972"

fun getKhaooGullyLink(): String = if (isAndroid()) KHAOO_GULLY_ANDROID_LINK else KHAOO_GULLY_IOS_LINK
