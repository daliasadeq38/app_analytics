package com.example.appanalytics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.appanalytics.adapter.AdapterCategory
import com.example.appanalytics.databinding.ActivityMainBinding
import com.example.appanalytics.listener.OnClickCategory
import com.example.appanalytics.model.Categories
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

const val TAG = "DocumentSnapshotTag"
class MainActivity : AppCompatActivity(),OnClickCategory {
    lateinit var binding : ActivityMainBinding
    lateinit var adapterCategory: AdapterCategory
    lateinit var arrayCategory : ArrayList<Categories>
    lateinit var listener: OnClickCategory
    lateinit var db : FirebaseFirestore

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var screenTimer: ScreenTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        arrayCategory = ArrayList()
        listener = this
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        screenTimer = ScreenTimer(FirebaseAnalytics.getInstance(this))
        analytics = Firebase.analytics

        trackScreenEventP()


        db.collection("Category")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val category = Categories(document.getString("id"),document.getString("name"))
                    arrayCategory.add(category)
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
        screenTimer.startTimer("MainActivity")
    }

    override fun onPause() {
        super.onPause()
        screenTimer.stopTimer()
    }

    private fun setContactInRecyclerView(){
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        adapterCategory = AdapterCategory(arrayCategory,listener)
        binding.recyclerView.adapter = adapterCategory

    }

    private fun trackScreenEventP() {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Categories")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }

    override fun onClick(name: String) {
        selectContentItem(UUID.randomUUID().toString(),name,"Click Item Category")
        startActivity(Intent(this@MainActivity,NotesActivity::class.java).putExtra("name",name))

    }

    private fun selectContentItem(id:String, name:String, contentType:String){
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT){
            param(FirebaseAnalytics.Param.ITEM_ID,id)
            param(FirebaseAnalytics.Param.ITEM_NAME,name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE,contentType)
        }
    }
}