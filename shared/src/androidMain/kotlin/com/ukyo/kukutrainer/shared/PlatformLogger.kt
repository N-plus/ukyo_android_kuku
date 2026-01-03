package com.ukyo.kukutrainer.shared

import android.util.Log

actual class PlatformLogger {
    actual fun log(tag: String, message: String) {
        Log.d(tag, message)
    }
}
