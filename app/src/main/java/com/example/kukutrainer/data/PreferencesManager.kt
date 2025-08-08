package com.example.kukutrainer.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Wrapper for [SharedPreferences] to persist app settings and progress.
 */
object PreferencesManager {
    private const val PREF_NAME = "kuku_prefs"

    private const val KEY_SELECTED_CHARACTER = "selected_character"
    private const val KEY_SOUND_SPEED = "sound_speed"
    private const val KEY_BGM_ON = "bgm_on"
    private const val KEY_TERMS_ACCEPTED = "terms_accepted"
    private const val KEY_VOICE_ON = "voice_on"
    private const val KEY_TOTAL_STUDY_TIME = "total_study_time"
    private fun stageCompletedKey(stage: Int) = "stage_completed_" + stage
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_SESSION_START_TIME = "session_start_time"
    private fun starCountKey(stage: Int) = "star_count_" + stage
    private fun quizCompletedKey(quiz: Int) = "quiz_completed_" + quiz

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setSelectedCharacter(context: Context, id: Int) {
        prefs(context).edit().putInt(KEY_SELECTED_CHARACTER, id).apply()
    }

    fun getSelectedCharacter(context: Context): Int =
        prefs(context).getInt(KEY_SELECTED_CHARACTER, 0)

    fun setStageCompleted(context: Context, stage: Int, completed: Boolean) {
        prefs(context).edit().putBoolean(stageCompletedKey(stage), completed).apply()
    }

    fun isStageCompleted(context: Context, stage: Int): Boolean =
        prefs(context).getBoolean(stageCompletedKey(stage), false)

    fun setStarCount(context: Context, stage: Int, count: Int) {
        prefs(context).edit().putInt(starCountKey(stage), count).apply()
    }

    fun getStarCount(context: Context, stage: Int): Int =
        prefs(context).getInt(starCountKey(stage), 0)
    fun setQuizCompleted(context: Context, quiz: Int, completed: Boolean) {
        prefs(context).edit().putBoolean(quizCompletedKey(quiz), completed).apply()
    }

    fun isQuizCompleted(context: Context, quiz: Int): Boolean =
        prefs(context).getBoolean(quizCompletedKey(quiz), false)

    fun setSoundSpeed(context: Context, speed: Float) {
        prefs(context).edit().putFloat(KEY_SOUND_SPEED, speed).apply()
    }

    fun getSoundSpeed(context: Context): Float =
        prefs(context).getFloat(KEY_SOUND_SPEED, 1.0f)

    fun setBgmOn(context: Context, on: Boolean) {
        prefs(context).edit().putBoolean(KEY_BGM_ON, on).apply()
    }

    fun isBgmOn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_BGM_ON, true)

    fun setTermsAccepted(context: Context, accepted: Boolean) {
        prefs(context).edit().putBoolean(KEY_TERMS_ACCEPTED, accepted).apply()
    }

    fun isTermsAccepted(context: Context): Boolean =
        prefs(context).getBoolean(KEY_TERMS_ACCEPTED, false)

    fun setVoiceOn(context: Context, on: Boolean) {
        prefs(context).edit().putBoolean(KEY_VOICE_ON, on).apply()
    }

    fun isVoiceOn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_VOICE_ON, true)

    fun setSessionStartTime(context: Context, time: Long) {
        prefs(context).edit().putLong(KEY_SESSION_START_TIME, time).apply()
    }

    fun getSessionStartTime(context: Context): Long =
        prefs(context).getLong(KEY_SESSION_START_TIME, 0L)

    fun addStudyTime(context: Context, millis: Long) {
        val total = getTotalStudyTime(context) + millis
        prefs(context).edit().putLong(KEY_TOTAL_STUDY_TIME, total).apply()
    }

    fun getTotalStudyTime(context: Context): Long =
        prefs(context).getLong(KEY_TOTAL_STUDY_TIME, 0L)

    fun setUserName(context: Context, name: String) {
        prefs(context).edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserName(context: Context): String =
        prefs(context).getString(KEY_USER_NAME, "") ?: ""
}