package com.example.appanalytics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.appanalytics.adapter.AdapterNote
import com.example.appanalytics.databinding.ActivityNotesBinding
import com.example.appanalytics.listener.OnClickNote
import com.example.appanalytics.model.Note
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class NotesActivity : AppCompatActivity(), OnClickNote {

    private lateinit var binding: ActivityNotesBinding
    private lateinit var db : FirebaseFirestore
    lateinit var arrayNote : ArrayList<Note>
    lateinit var adapterNote: AdapterNote
    private lateinit var listener:OnClickNote

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var screenTimer: ScreenTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore
        arrayNote = ArrayList()
        listener = this
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE


        screenTimer = ScreenTimer(FirebaseAnalytics.getInstance(this))
        analytics = Firebase.analytics

        trackScreenEventP()



        val category = intent.getStringExtra("name")
        Toast.makeText(this@NotesActivity, "name: $category", Toast.LENGTH_SHORT).show()
        getDate(category!!)

    }
    private fun getDate(category: String){
        db.collection(category)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val note = Note(
                        document.getString("id"),
                        document.getString("tile"),
                        document.getString("description"),
                        document.getString("Url"),
                        document.getString("category"),
                    )
                    arrayNote.add(note)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                setContactInRecyclerView()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    override fun onResume() {
        super.onResume()
        screenTimer.startTimer("NotesActivity")
    }

    override fun onPause() {
        super.onPause()
        screenTimer.stopTimer()
    }

    private fun setContactInRecyclerView(){
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        adapterNote = AdapterNote(arrayNote,listener)
        binding.recyclerView.adapter = adapterNote
    }

    private fun trackScreenEventP() {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Notes")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "NotesActivity")
        }
    }

    override fun onClick(note: Note) {
        selectContentItem(UUID.randomUUID().toString(),note.title!!,"Click Item Note")
        startActivity(Intent(this@NotesActivity,DetailsActivity::class.java).putExtra("note",note))
    }

    private fun selectContentItem(id:String, name:String, contentType:String){
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT){
            param(FirebaseAnalytics.Param.ITEM_ID,id)
            param(FirebaseAnalytics.Param.ITEM_NAME,name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE,contentType)
        }
    }
}