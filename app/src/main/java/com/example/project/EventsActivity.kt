package com.example.project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar  // Added import for Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.EventAdapter
import com.example.project.R
import com.google.firebase.firestore.FirebaseFirestore

class EventsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        firestore = FirebaseFirestore.getInstance()

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)  // Added Toolbar setup
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Initialize UI components for posting events
        val titleEditText: EditText = findViewById(R.id.titleEditText)
        val descriptionEditText: EditText = findViewById(R.id.descriptionEditText)
        val dateEditText: EditText = findViewById(R.id.dateEditText)
        val timeEditText: EditText = findViewById(R.id.timeEditText)
        val locationEditText: EditText = findViewById(R.id.locationEditText)
        val postButton: Button = findViewById(R.id.postButton)

        postButton.setOnClickListener { postEvent() }

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

    private fun postEvent() {
        val title = findViewById<EditText>(R.id.titleEditText).text.toString()
        val description = findViewById<EditText>(R.id.descriptionEditText).text.toString()
        val date = findViewById<EditText>(R.id.dateEditText).text.toString()
        val time = findViewById<EditText>(R.id.timeEditText).text.toString()
        val location = findViewById<EditText>(R.id.locationEditText).text.toString()

        val event = hashMapOf(
            "title" to title,
            "description" to description,
            "date" to date,
            "time" to time,
            "location" to location
        )

        firestore.collection("events")
            .add(event)
            .addOnSuccessListener {
                Toast.makeText(this, "Event posted successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to post event", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        findViewById<EditText>(R.id.titleEditText).text.clear()
        findViewById<EditText>(R.id.descriptionEditText).text.clear()
        findViewById<EditText>(R.id.dateEditText).text.clear()
        findViewById<EditText>(R.id.timeEditText).text.clear()
        findViewById<EditText>(R.id.locationEditText).text.clear()
    }
}
