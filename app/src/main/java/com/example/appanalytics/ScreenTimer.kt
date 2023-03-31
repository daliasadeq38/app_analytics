package com.example.appanalytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class ScreenTimer(private val firebaseAnalytics: FirebaseAnalytics) {
    private var startTime: Long = 0
    private var screenName: String? = null

    fun startTimer(screenName: String) {
        // If there is already a screen being timed, stop it first
        if (this.screenName != null) {
            stopTimer()
        }
        // Start timing the new screen
        this.startTime = System.currentTimeMillis()
        this.screenName = screenName
    }

    fun stopTimer() {
        if (this.screenName != null) {
            val elapsedTime = System.currentTimeMillis() - startTime
            val bundle = Bundle().apply {
                putString("screen_name", screenName)
                putLong("screen_time", elapsedTime)
            }
            firebaseAnalytics.logEvent("screen_time", bundle)
            this.screenName = null
        }
    }
}