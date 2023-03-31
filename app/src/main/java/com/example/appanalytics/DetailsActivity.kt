package com.example.appanalytics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appanalytics.databinding.ActivityDetailsBinding
import com.example.appanalytics.model.Note
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var screenTimer: ScreenTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenTimer = ScreenTimer(FirebaseAnalytics.getInstance(this))
        analytics = Firebase.analytics

        trackScreenEventP()

        val note = intent.getSerializableExtra("note") as Note
        Toast.makeText(this@DetailsActivity, "title: ${note.title}", Toast.LENGTH_SHORT).show()

        binding.categoryNote.setText(note.category)
        binding.titleNote.setText(note.title)
        binding.descriptionNote.setText(note.description)
        Picasso.get().load(note.url).into(binding.imageNote)

    }

    private fun trackScreenEventP() {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Details")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "DetailsActivity")
        }
    }

    override fun onResume() {
        super.onResume()
        screenTimer.startTimer("DetailsActivity")
    }

    override fun onPause() {
        super.onPause()
        screenTimer.stopTimer()
    }
}