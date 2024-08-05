package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val programTextView: TextView = findViewById(R.id.programTextView)
        val studentIdTextView: TextView = findViewById(R.id.studentIdTextView)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem)
            true
        }

        // Get current user
        val user = auth.currentUser
        user?.let {
            // Fetch user details from Firestore
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name") ?: "N/A"
                        val email = document.getString("email") ?: "N/A"
                        val program = document.getString("program") ?: "N/A"
                        val studentId = document.getString("student_id") ?: "N/A"

                        // Update UI with user details
                        nameTextView.text = "Name: $name"
                        emailTextView.text = "Email: $email"
                        programTextView.text = "Program: $program"
                        studentIdTextView.text = "Student Id: $studentId"
                    } else {
                        Toast.makeText(this, "No such user found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
