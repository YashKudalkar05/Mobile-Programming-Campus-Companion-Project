package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class EventsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        firestore = FirebaseFirestore.getInstance()

        // Setup bottom navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem)
            true
        }


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



    private fun handleMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                home()
                true
            }
            R.id.user_details -> {
                // Handle study groups navigation
                profile()
                true
            }
            R.id.logout -> {
                logout()
                true
            }
            else -> false
        }
    }
    private fun logout() {;
        val auth = FirebaseAuth.getInstance()

        auth.signOut()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }
    private fun home() {;
        // Navigate back to login activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }
    private fun profile() {;
        // Navigate back to login activity
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }

}
