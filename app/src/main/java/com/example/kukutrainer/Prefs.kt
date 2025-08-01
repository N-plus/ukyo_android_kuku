package com.example.kukutrainer

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "kuku_prefs"
private const val KEY_SELECTED_CHARACTER = "selected_character"

fun Context.getPrefs(): SharedPreferences =
    getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

fun Context.saveSelectedCharacter(id: Int) {
    getPrefs().edit().putInt(KEY_SELECTED_CHARACTER, id).apply()
}

fun Context.loadSelectedCharacter(): Int =
    getPrefs().getInt(KEY_SELECTED_CHARACTER, -1)