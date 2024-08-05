package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddEventActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_event)
        firestore = FirebaseFirestore.getInstance()

        // Initialize UI components for posting events
        // Setup bottom navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem)
            true
        }
        val postButton: Button = findViewById(R.id.postButton)

        postButton.setOnClickListener { postEvent() }

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


    private fun handleMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                // Handle event navigation
                true
            }
            R.id.user_details -> {
                // Handle study groups navigation
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
}
