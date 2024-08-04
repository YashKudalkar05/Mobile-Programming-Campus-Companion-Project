package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class EventsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        firestore = FirebaseFirestore.getInstance()


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)
        }

        // Initialize UI components for viewing events
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = EventAdapter()
        recyclerView.adapter = adapter



        firestore.collection("events")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("EventsActivity", "Failed to load events", e)  // Log the error
                    Toast.makeText(this, "Failed to load events: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val events = snapshot.documents.mapNotNull { it.toObject(Event::class.java) }
                    adapter.submitList(events)
                } else {
                    Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show()
                }
            }
    }


}
