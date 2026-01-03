package com.ukyo.kukutrainer.shared

expect class PlatformLogger() {
    fun log(tag: String, message: String)
}
